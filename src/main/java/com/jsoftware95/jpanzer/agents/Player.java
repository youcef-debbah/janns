/*
 * Copyright 2017-2018 Youcef DEBBAH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jsoftware95.jpanzer.agents;

import com.jsoftware95.janns.EnumNeuralNetwork;
import com.jsoftware95.janns.api.DataMapper;
import com.jsoftware95.jpanzer.game.Context;
import com.jsoftware95.jpanzer.game.GameClientConnection;
import com.jsoftware95.jpanzer.game.GameListener;
import com.jsoftware95.jpanzer.game.Orders;
import com.jsoftware95.jpanzer.game.ScoreListener;
import com.jsoftware95.jpanzer.game.Tank;
import com.jsoftware95.jpanzer.game.TankClient;
import com.jsoftware95.toolkit.InputMappers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("ALL")
public class Player {
    public static final int DELAY_BETWEEN_GAMES = 750;
    private static final Logger log = LogManager.getLogger(Player.class);
    private static final int THREADS_COUNT = 10;
    private static final int TIME_BETWEEN_ORDERS = 20;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss");
    private static final String FOLDER = "players/";
    private static final DataMapper NEW_BRAIN_DATA = (value, cloneIndex, valueIndex) -> cloneIndex != valueIndex ? value : Double.NaN;
    private static final DataMapper IDENTITY_DATA = (value, cloneIndex, valueIndex) -> value;

    static {
        File folder = new File(FOLDER);
        for (File file : folder.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

    private final ExecutorService EVOLVER = Executors.newSingleThreadExecutor();

    private final FintessCalculator calculator = new FintessCalculator();
    private final String name;
    protected volatile EnumNeuralNetwork<Inputs, Outputs> brain;
    private int generation;
    private PlayingTask currentPlayingTask;
    private volatile boolean isEvolving;
    private boolean alwaysEvolve;

    public Player(final String name) {
        this(name, false);
    }

    public Player(final String name, final boolean alwaysEvolve) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Player name can't be null or empty");

        this.name = name;
        this.alwaysEvolve = alwaysEvolve;

        brain = new EnumNeuralNetwork<>(Inputs.values(), Outputs.values());
        brain.addHiddenLayer(Inputs.values().length * 2);
    }

    private Player(final String name, final EnumNeuralNetwork<Inputs, Outputs> brain) {
        this.name = Objects.requireNonNull(name);
        this.brain = Objects.requireNonNull(brain);
    }

    public static void main(final String... args) {
        final Player player = new Player("alpha");
        player.loadData("Player-Beta_g-337_at_21-13-27");

        System.out.println(player.getBrain().toString());
    }

    public String getPlayerName() {
        return name;
    }

    public int getGeneration() {
        return generation;
    }

    public void prepare(final GameClientConnection connection) {
        if (currentPlayingTask != null)
            currentPlayingTask.end();

        currentPlayingTask = new PlayingTask(getPlayerName(), connection, brain);
    }

    public void startPlaying() {
        if (currentPlayingTask == null)
            throw new IllegalStateException("This player is not prepared yet for playing");

        currentPlayingTask.start();
    }

    public void roundEnded(final boolean won, final Player opponent) {
        currentPlayingTask.end();

        if (alwaysEvolve || !won) {
            isEvolving = true;
            EVOLVER.execute(new Evolver(this, opponent.getBrain()));
        }
    }

    public void stopPlaying() {
        if (currentPlayingTask != null)
            currentPlayingTask.end();
        EVOLVER.shutdownNow();
    }

    public boolean stillPlaying() {
        if (currentPlayingTask == null)
            return !EVOLVER.isTerminated();
        else
            return currentPlayingTask.stillPlaying() && !EVOLVER.isTerminated();
    }

    private EnumNeuralNetwork<Inputs, Outputs> evolveBrain(EnumNeuralNetwork<Inputs, Outputs> opponent) {
        long t0 = System.nanoTime();
        generation++;

        final Deque<Double> data = brain.getData();
        final List<EnumNeuralNetwork<Inputs, Outputs>> brains = brain.massClone(data.size(), NEW_BRAIN_DATA);
        final List<EnumNeuralNetwork<Inputs, Outputs>> originalBrains = opponent.massClone(data.size(), IDENTITY_DATA);

        final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(THREADS_COUNT);

        for (int i = 1; i <= THREADS_COUNT; i++) {
            int blockTime = i * DELAY_BETWEEN_GAMES;
            THREAD_POOL.submit(() -> {
                try {
                    Thread.sleep(blockTime);
                } catch (InterruptedException ignored) {}
            });
        }

        int total = brains.size();
        int i = 0;
        List<Future<TestResult>> results = new ArrayList<>(brains.size());
        for (final EnumNeuralNetwork<Inputs, Outputs> newBrain : brains) {
            if (Thread.interrupted()) {
                generation--;
                THREAD_POOL.shutdownNow();
                return brain;
            }
            EnumNeuralNetwork<Inputs, Outputs> originalBrain = originalBrains.get(i);
            final FitnessTest fitnessTest = new FitnessTest(this, originalBrain, newBrain, String.format("%03d/%03d", i + 1, total));
            results.add(THREAD_POOL.submit(fitnessTest));
            i++;
        }

        THREAD_POOL.shutdown();

        NavigableMap<Double, EnumNeuralNetwork<Inputs, Outputs>> testedBrains = new TreeMap<>();
        for (final Future<TestResult> result : results) {
            if (Thread.interrupted()) {
                generation--;
                return brain;
            }
            try {
                TestResult testResult;
                if (THREADS_COUNT > 1) {
                    testResult = result.get((THREADS_COUNT - 1) * DELAY_BETWEEN_GAMES, TimeUnit.MILLISECONDS);
                } else {
                    testResult = result.get();
                }
                testedBrains.put(testResult.getFitness(), testResult.getTestSubject());
            } catch (TimeoutException e) {
                log.error("evolution timeout (candidate excluded)", e);
            } catch (InterruptedException | ExecutionException e) {
                log.error("could not calc fitness (candidate excluded)", e);
            }
        }

        log.debug("generation: " + generation + " evolved in: " + (System.nanoTime() - t0) / 1_000_000_000.0 + " sec");
        return testedBrains.lastEntry().getValue();
    }

    private double calcFitness(EnumNeuralNetwork<Inputs, Outputs> brain, EnumNeuralNetwork<Inputs, Outputs> newBrain, String title) {
        TankClient game = new TankClient("fitness test: " + title + " for: " + getFullName(), false);

        Player newPlayer = new Player("test subject: " + title, newBrain);
        Player originalPlayer = new Player(getPlayerName() + " clone: " + title, brain);

        newPlayer.prepare(game.getConnectionToTank1());
        originalPlayer.prepare(game.getConnectionToTank2());

        game.addListener(new Cleaner(newPlayer, originalPlayer));
        game.addScoreListener(calculator);

        newPlayer.startPlaying();
        originalPlayer.startPlaying();

        while (!Thread.currentThread().isInterrupted() &&
                (calculator.resultNotReady() || newPlayer.stillPlaying() || originalPlayer.stillPlaying()))
            waitForResult();

        if (Thread.currentThread().isInterrupted())
            return 0.0;
        else
            return calculator.getResult();
    }

    private void waitForResult() {
        try {
            log.trace("thread waiting for result: " + Thread.currentThread().getName());
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private String getFileName() {
        return "Player-" + getPlayerName().replaceAll("\\W+", "_") +
                "_g-" + generation + "_at_" + formatter.format(LocalTime.now());
    }

    private void save(Deque<Double> data, String fileName) {
        File file = new File(FOLDER + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.error("could not create file: " + file.getAbsolutePath());
            return;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeInt(generation);
            out.writeObject(data);
        } catch (IOException e) {
            log.error("could not save data: " + fileName, e);
        }
    }

    public String getFullName() {
        return getPlayerName() + " (generation: " + getGeneration() + ")";
    }

    public void loadData(String fileName) {
        Objects.requireNonNull(fileName);

        Deque<Double> oldData = getBrain().getData();
        File file = new File("bestPlayers/" + fileName);

        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
            this.generation = input.readInt();
            brain.setData((Deque<Double>) input.readObject());
        } catch (IOException | ClassNotFoundException e) {
            log.error("loading player failed: " + fileName, e);
            getBrain().setData(oldData);
        }
    }

    private EnumNeuralNetwork<Inputs, Outputs> getBrain() {
        return brain;
    }

    public void setBrain(EnumNeuralNetwork<Inputs, Outputs> brain) {
        this.brain = brain;
    }

    public boolean isEvolving() {
        return isEvolving;
    }

    public void setEvolving(boolean evolving) {
        isEvolving = evolving;
    }

    private static final class FintessCalculator implements ScoreListener {

        private int result;
        private volatile boolean resultReady;

        @Override
        public void roundEndedWithScore(final int newPlayer, final int originalPlayer) {
            result = newPlayer;
            resultReady = true;
        }

        public boolean resultNotReady() {
            return !resultReady;
        }

        public int getResult() {
            resultReady = false;
            return result;
        }
    }

    private static final class Cleaner implements GameListener {

        private Player p1;
        private Player p2;

        private Cleaner(Player p1, Player p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public void roundEnded(boolean p1Won, boolean p2Won) {
            if (p1 != null) {
                p1.stopPlaying();
                p1 = null;
            }

            if (p2 != null) {
                p2.stopPlaying();
                p2 = null;
            }
        }
    }

    private static final class FitnessTest implements Callable<TestResult> {

        private Player player;
        private EnumNeuralNetwork<Inputs, Outputs> originalBrain;
        private EnumNeuralNetwork<Inputs, Outputs> newBrain;
        private String title;
        private TestResult testResult;

        public FitnessTest(Player player, EnumNeuralNetwork<Inputs, Outputs> originalBrain,
                           EnumNeuralNetwork<Inputs, Outputs> newBrain, String title) {
            this.player = Objects.requireNonNull(player);
            this.originalBrain = Objects.requireNonNull(originalBrain);
            this.newBrain = Objects.requireNonNull(newBrain);
            this.title = title;
        }

        @Override
        public TestResult call() throws Exception {
            if (testResult == null) {
                testResult = new TestResult(player.calcFitness(originalBrain, newBrain, title), newBrain);
                player = null;
                originalBrain = null;
                newBrain = null;
            }
            return testResult;
        }
    }

    private static final class TestResult {
        private double fitness;
        private EnumNeuralNetwork<Inputs, Outputs> testSubject;

        public TestResult(double fitness, EnumNeuralNetwork<Inputs, Outputs> testSubject) {
            this.fitness = Objects.requireNonNull(fitness);
            this.testSubject = Objects.requireNonNull(testSubject);
        }

        public double getFitness() {
            return fitness;
        }

        public EnumNeuralNetwork<Inputs, Outputs> getTestSubject() {
            EnumNeuralNetwork<Inputs, Outputs> result = testSubject;
            testSubject = null;
            return result;
        }

    }

    private static final class Evolver implements Runnable, AutoCloseable {

        private Player player;
        private EnumNeuralNetwork<Inputs, Outputs> opponent;

        public Evolver(Player player, EnumNeuralNetwork<Inputs, Outputs> opponent) {
            this.player = Objects.requireNonNull(player);
            this.opponent = Objects.requireNonNull(opponent);
        }

        @Override
        public void run() {
            ensureOpen();

            EnumNeuralNetwork<Inputs, Outputs> oldBrain = player.getBrain();
            player.setBrain(player.evolveBrain(opponent));

            if (oldBrain == player.getBrain()) {
                log.debug("evolving interrupted");
                player.setEvolving(false);
                return;
            }

            Deque<Double> data = player.getBrain().getData();
            log.debug("Player: " + player.getPlayerName() + " has just evolved!");
            player.save(data, player.getFileName());

            player.setEvolving(false);
            close();
        }

        private void ensureOpen() {
            if (player == null)
                throw new IllegalStateException("this evolver is already closed");
        }

        @Override
        public void close() {
            player = null;
        }

        public boolean isClosed() {
            return player == null;
        }
    }

    private static final class PlayingTask implements Runnable {
        private static final int minDistanceX = -TankClient.Fram_width;
        private static final int maxDistanceX = TankClient.Fram_width;
        private static final int minDistanceY = -TankClient.Fram_length;
        private static final int maxDistanceY = TankClient.Fram_length;

        private final ExecutorService MAIN_WORKER = Executors.newSingleThreadExecutor();
        private final String playerName;
        private final GameClientConnection connection;
        private EnumNeuralNetwork<Inputs, Outputs> brain;

        private PlayingTask(final String playerName, final GameClientConnection connection, final EnumNeuralNetwork<Inputs, Outputs> brain) {
            this.playerName = Objects.requireNonNull(playerName);
            this.connection = Objects.requireNonNull(connection);
            this.brain = Objects.requireNonNull(brain);
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    sendInputsToBrain(connection.getContext());
                    brain.processInput();
                    connection.sendOrders(getOrdersFromBrain());

                    try {
                        Thread.sleep(TIME_BETWEEN_ORDERS);
                    } catch (final InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }

                }
            } finally {
                connection.close();
            }
        }

        private void sendInputsToBrain(final Context context) {
            final int rawSpeedX = context.getSpeedX();
            final int rawSpeedY = context.getSpeedY();
            final double speedX = InputMappers.LINEAR.map(rawSpeedX, -Tank.MAX_SPEED_X, Tank.MAX_SPEED_X);
            final double speedY = InputMappers.LINEAR.map(rawSpeedY, -Tank.MAX_SPEED_Y, Tank.MAX_SPEED_Y);

            //System.out.println("# " + playerName + " (rawX=" + rawSpeedX + ", rawY=" + rawSpeedY + ") -> (x=" + speedX + ", y=" + speedY + ")");
            double speed = speedX + speedY;
            double axis;
            if (speedX != 0 && speedY == 0)
                axis = -1.0;
            else if (speedX == 0 && speedY != 0)
                axis = 1.0;
            else
                axis = 0.0;

            brain.setInput(Inputs.SPEED, InputMappers.LINEAR.map(speed, -1, 1));
            brain.setInput(Inputs.AXIS, axis);

            final boolean loaded = context.isLoaded();
            final boolean enemyLoaded = context.isEnemyLoaded();
            final int enemySpeedY = context.getEnemySpeedY();
            final int distanceFromEnemyY = context.getDistanceFromEnemyY();

            brain.setInput(Inputs.IS_LOADED, loaded);
            brain.setInput(Inputs.IS_ENEMY_LOADED, enemyLoaded);
            brain.setInput(Inputs.ENEMY_SPEED_Y, enemySpeedY, -Tank.MAX_SPEED_Y, Tank.MAX_SPEED_Y);
            brain.setInput(Inputs.DISTANCE_FROM_ENEMY_Y, distanceFromEnemyY, minDistanceY, maxDistanceY);

            log.trace(String.format("%s inputs: { speed=%.2f, axis=%.2f, loaded=%b, enemyLoaded=%b, enemySpeedY=%d, distance=%d }",
                    playerName, speed, axis, loaded, enemyLoaded, enemySpeedY, distanceFromEnemyY));
        }

        private Orders getOrdersFromBrain() {
            final Orders orders = new Orders();

            double speed = brain.getOutput(Outputs.SPEED);
            double axis = brain.getOutput(Outputs.AXIS);

            if (speed > 0) {
                if (axis > 0)
                    orders.setMoveDown(true);
                else if (axis < 0)
                    orders.setMoveRight(true);
            } else if (speed < 0) {
                if (axis > 0)
                    orders.setMoveUp(true);
                else if (axis < 0)
                    orders.setMoveLeft(true);
            }

            orders.setFire(brain.getOutputAsBoolean(Outputs.FIRE));
            log.trace(playerName + " orders: " + orders);

            return orders;
        }

        public void end() {
            MAIN_WORKER.shutdownNow();
        }

        public void start() {
            MAIN_WORKER.execute(this);
        }

        public boolean stillPlaying() {
            return !MAIN_WORKER.isTerminated();
        }
    }

}

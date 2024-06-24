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

import com.jsoftware95.jpanzer.game.TankClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("MethodParameterOfConcreteClass")
public class Competition {

    private static final Logger log = LogManager.getLogger(Competition.class);
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final Player player1;
    private final Player player2;
    private boolean competitionsAvailable = true;

    public Competition(final Player player1, final Player player2) {
        this.player1 = Objects.requireNonNull(player1);
        this.player2 = Objects.requireNonNull(player2);
    }

    public void start() {
        competitionsAvailable = true;
        executor.execute(this::keepStartingGames);
    }

    private void keepStartingGames() {
        if (competitionsAvailable) {
            while (player1.isEvolving() || player2.isEvolving())
                waitForThem();

            final TankClient game = new TankClient(player1.getFullName() + " VS " + player2.getFullName(), false);

            game.addListener((p1, p2) -> {
                player1.roundEnded(p1, player2);
                player2.roundEnded(p2, player1);
                executor.execute(this::keepStartingGames);
            });

            player1.prepare(game.getConnectionToTank1());
            player2.prepare(game.getConnectionToTank2());

            player1.startPlaying();
            player2.startPlaying();
        }
    }

    private void waitForThem() {
        try {
            log.trace("thread waiting for players to evolve: " + Thread.currentThread().getName());
            Thread.sleep(10);
        } catch (final InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        if (Thread.interrupted()) {
            player1.stopPlaying();
            player2.stopPlaying();
        }
    }

    public void end() {
        competitionsAvailable = false;
        executor.shutdownNow();
    }
}

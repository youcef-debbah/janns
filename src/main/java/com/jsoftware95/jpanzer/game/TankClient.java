package com.jsoftware95.jpanzer.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("ALL")
public class TankClient extends Frame implements ActionListener {

    public static final int Fram_width = 800;
    public static final int Fram_length = 600;
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final Image background = tk.getImage(TankClient.class.getResource("images/background.png"));
    private static final int backgroundWidth = 384;
    private static final int backgroundLength = 80;
    private static final Logger log = LogManager.getLogger(TankClient.class);
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static int MAX_TIME = 300;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    public boolean printable = true;
    MenuBar jmb;
    Menu jm1, jm2, jm3, jm4, jm5;
    MenuItem jmi1, jmi2, jmi3, jmi4, jmi5,
            jmi6, jmi7, jmi8, jmi9, jmi10, jmi11;
    Image screenImage;

    double initY1;
    double initY2;
    boolean normalOrReverse = true;
    Tank homeTank;
    Tank homeTank2;
    Boolean Player2 = true;
    GetBlood blood = new GetBlood();
    Home home = new Home(-100, -100, this);
    Boolean win = false, lose = false;
    boolean roundEnded, p1Won, p2Won;
    List<River> theRiver = new ArrayList<River>();
    List<Tank> tanks = new ArrayList<Tank>();
    List<BombTank> bombTanks = new ArrayList<BombTank>();
    List<Bullets> bullets = new ArrayList<Bullets>();
    List<Tree> trees = new ArrayList<Tree>();
    List<CommonWall> homeWall = new ArrayList<CommonWall>();
    List<CommonWall> otherWall = new ArrayList<CommonWall>();
    List<MetalWall> metalWall = new ArrayList<MetalWall>();
    volatile int currentTime;
    List<GameListener> gameListeners = new LinkedList<>();
    List<ScoreListener> scoreListeners = new LinkedList<>();
    private boolean manualControls;
    private long upTime;

    {
        int length = Math.max(Tank.length, Tank.width);

        initY1 = 3 * length + (Fram_length - 5 * length) * Math.random();
        initY2 = 3 * length + (Fram_length - 5 * length) * Math.random();

        while (Math.abs(initY1 - initY2) < (2 * length))
            initY2 = 3 * length + (Fram_length - 5 * length) * Math.random();

        homeTank = new Tank(normalOrReverse ? 150 : Fram_width - 150, (int) initY1, true, Direction.STOP, this, 1);
        homeTank2 = new Tank(normalOrReverse ? Fram_width - 150 : 150, (int) initY2, true, Direction.STOP, this, 2);
    }

    public TankClient(String title, boolean manualControls) {
        super(title);
        upTime = System.nanoTime();
        this.manualControls = manualControls;
        // printable = false;

        setAutoRequestFocus(false);
        toBack();

        jmb = new MenuBar();
        jm1 = new Menu("Game");
        jm2 = new Menu("Pause/Continue");
        jm3 = new Menu("Help");
        jm4 = new Menu("Level");
        jm5 = new Menu("Addition");
        jm1.setFont(new Font("Times New Roman", Font.BOLD, 15));
        jm2.setFont(new Font("Times New Roman", Font.BOLD, 15));
        jm3.setFont(new Font("Times New Roman", Font.BOLD, 15));
        jm4.setFont(new Font("Times New Roman", Font.BOLD, 15));

        jmi1 = new MenuItem("New Game");
        jmi2 = new MenuItem("Exit");
        jmi3 = new MenuItem("Stop");
        jmi4 = new MenuItem("Continue");
        jmi5 = new MenuItem("Help");
        jmi6 = new MenuItem("Level1");
        jmi7 = new MenuItem("Level2");
        jmi8 = new MenuItem("Level3");
        jmi9 = new MenuItem("Level4");
        jmi10 = new MenuItem("Add Player 2");
        jmi11 = new MenuItem("Join other's game");
        jmi1.setFont(new Font("Times New Roman", Font.BOLD, 15));
        jmi2.setFont(new Font("Times New Roman", Font.BOLD, 15));
        jmi3.setFont(new Font("Times New Roman", Font.BOLD, 15));
        jmi4.setFont(new Font("Times New Roman", Font.BOLD, 15));
        jmi5.setFont(new Font("Times New Roman", Font.BOLD, 15));

        jm1.add(jmi1);
        jm1.add(jmi2);
        jm2.add(jmi3);
        jm2.add(jmi4);
        jm3.add(jmi5);
        jm4.add(jmi6);
        jm4.add(jmi7);
        jm4.add(jmi8);
        jm4.add(jmi9);
        jm5.add(jmi10);
        jm5.add(jmi11);

        jmb.add(jm1);
        jmb.add(jm2);

        //jmb.add(jm4);
        //jmb.add(jm5);
        //jmb.add(jm3);

        jmi1.addActionListener(this);
        jmi1.setActionCommand("NewGame");
        jmi2.addActionListener(this);
        jmi2.setActionCommand("Exit");
        jmi3.addActionListener(this);
        jmi3.setActionCommand("Stop");
        jmi4.addActionListener(this);
        jmi4.setActionCommand("Continue");
        jmi5.addActionListener(this);
        jmi5.setActionCommand("help");
        jmi6.addActionListener(this);
        jmi6.setActionCommand("level1");
        jmi7.addActionListener(this);
        jmi7.setActionCommand("level2");
        jmi8.addActionListener(this);
        jmi8.setActionCommand("level3");
        jmi9.addActionListener(this);
        jmi9.setActionCommand("level4");
        jmi10.addActionListener(this);
        jmi10.setActionCommand("Player2");
        jmi11.addActionListener(this);
        jmi11.setActionCommand("Join");

        this.setMenuBar(jmb);
/*
        for (int i = 0; i < 10; i++) {
            if (i < 4)
                homeWall.add(new CommonWall(350, 580 - 21 * i, this));
            else if (i < 7)
                homeWall.add(new CommonWall(372 + 22 * (i - 4), 517, this));
            else
                homeWall.add(new CommonWall(416, 538 + (i - 7) * 21, this));

        }

        for (int i = 0; i < 32; i++) {
            if (i < 16) {
                otherWall.add(new CommonWall(200 + 21 * i, 300, this));
                otherWall.add(new CommonWall(500 + 21 * i, 180, this));
                otherWall.add(new CommonWall(200, 400 + 21 * i, this));
                otherWall.add(new CommonWall(500, 400 + 21 * i, this));
            } else if (i < 32) {
                otherWall.add(new CommonWall(200 + 21 * (i - 16), 320, this));
                otherWall.add(new CommonWall(500 + 21 * (i - 16), 220, this));
                otherWall.add(new CommonWall(222, 400 + 21 * (i - 16), this));
                otherWall.add(new CommonWall(522, 400 + 21 * (i - 16), this));
            }
        }

        for (int i = 0; i < 20; i++) {
            if (i < 10) {
                metalWall.add(new MetalWall(140 + 30 * i, 150, this));
                metalWall.add(new MetalWall(600, 400 + 20 * (i), this));
            } else if (i < 20)
                metalWall.add(new MetalWall(140 + 30 * (i - 10), 180, this));

        }

        for (int i = 0; i < 4; i++) {
            if (i < 4) {
                trees.add(new Tree(0 + 30 * i, 360, this));
                trees.add(new Tree(220 + 30 * i, 360, this));
                trees.add(new Tree(440 + 30 * i, 360, this));
                trees.add(new Tree(660 + 30 * i, 360, this));
            }

        }

*/
        /*
        for (int i = 0; i < 20; i++) {
            if (i < 9)
                tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this, 0));
            else if (i < 15)
                tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D,
                        this, 0));
            else
                tanks
                        .add(new Tank(10, 50 * (i - 12), false, Direction.D,
                                this, 0));
        }
        */

        theRiver.add(new River((Fram_width - River.riverWidth) / 2, 0, this));

        this.setSize(Fram_width, Fram_length);
        this.setLocation(280, 50);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setResizable(false);
        this.setBackground(Color.GRAY);
        this.setVisible(true);

        if (manualControls)
            this.addKeyListener(new KeyMonitor());

        log.debug("starting game: " + getTitle());
        printable = true;
        threadPool.execute(new PaintTask(this));
    }

    public void showClient() {
        setVisible(true);
    }

    public GameClientConnection getConnectionToTank1() {
        return new TankConnection(this, homeTank, homeTank2);
    }

    public GameClientConnection getConnectionToTank2() {
        return new TankConnection(this, homeTank2, homeTank);
    }

    public boolean isManualControls() {
        return manualControls;
    }

    public void update(Graphics g) {
        screenImage = this.createImage(Fram_width, Fram_length);

        Graphics gps = screenImage.getGraphics();
        drawBackground(gps);
        framPaint(gps);
        g.drawImage(screenImage, 0, 0, null);
    }

    private void drawBackground(Graphics g) {
        int currentX = 0;
        int currentY = 0;
        while (currentX < Fram_width) {
            while (currentY < Fram_length) {
                g.drawImage(background, currentX, currentY, null);
                currentY += backgroundLength;
            }
            currentY = 0;
            currentX += backgroundWidth;
        }
    }

    public void framPaint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHints(rh);

        Color c = g.getColor();
        Font f1 = g.getFont();
        g.setColor(Color.BLACK);
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));

        for (int i = 0; i < theRiver.size(); i++) {
            River r = theRiver.get(i);
            r.draw(g);
        }

        for (int i = 0; i < theRiver.size(); i++) {
            River r = theRiver.get(i);
            homeTank.collideRiver(r);
            if (Player2) homeTank2.collideRiver(r);
            r.draw(g);
        }

        home.draw(g);

        homeTank.drawAimLine(g);
        if (Player2)
            homeTank2.drawAimLine(g);

        homeTank.draw(g);
        homeTank.eat(blood);
        if (Player2) {
            homeTank2.draw(g);
            homeTank2.eat(blood);
        }

        bullets.removeIf(Objects::isNull);

        for (int i = 0; i < bullets.size(); i++) {
            Bullets m = bullets.get(i);
            m.hitTanks(tanks);
            m.hitTank(homeTank);
            m.hitTank(homeTank2);
            m.hitHome();
            for (int j = 0; j < bullets.size(); j++) {
                if (i == j) continue;
                Bullets bts = bullets.get(j);
                m.hitBullet(bts);
            }
            for (int j = 0; j < metalWall.size(); j++) {
                MetalWall mw = metalWall.get(j);
                m.hitWall(mw);
            }

            for (int j = 0; j < otherWall.size(); j++) {
                CommonWall w = otherWall.get(j);
                m.hitWall(w);
            }

            for (int j = 0; j < homeWall.size(); j++) {
                CommonWall cw = homeWall.get(j);
                m.hitWall(cw);
            }
            m.draw(g);
        }

        for (int i = 0; i < tanks.size(); i++) {
            Tank t = tanks.get(i);

            for (int j = 0; j < homeWall.size(); j++) {
                CommonWall cw = homeWall.get(j);
                t.collideWithWall(cw);
                cw.draw(g);
            }
            for (int j = 0; j < otherWall.size(); j++) {
                CommonWall cw = otherWall.get(j);
                t.collideWithWall(cw);
                cw.draw(g);
            }
            for (int j = 0; j < metalWall.size(); j++) {
                MetalWall mw = metalWall.get(j);
                t.collideWithWall(mw);
                mw.draw(g);
            }
            for (int j = 0; j < theRiver.size(); j++) {
                River r = theRiver.get(j);
                t.collideRiver(r);
                r.draw(g);
                // t.draw(g);
            }

            t.collideWithTanks(tanks);
            t.collideHome(home);

            t.draw(g);
        }

        //blood.draw(g);

        for (int i = 0; i < trees.size(); i++) {
            Tree tr = trees.get(i);
            tr.draw(g);
        }

        for (int i = 0; i < bombTanks.size(); i++) {
            BombTank bt = bombTanks.get(i);
            bt.draw(g);
        }

        for (int i = 0; i < otherWall.size(); i++) {
            CommonWall cw = otherWall.get(i);
            cw.draw(g);
        }

        for (int i = 0; i < metalWall.size(); i++) {
            MetalWall mw = metalWall.get(i);
            mw.draw(g);
        }

        homeTank.collideWithTanks(tanks);
        homeTank.collideHome(home);
        if (Player2) {
            homeTank2.collideWithTanks(tanks);
            homeTank2.collideHome(home);
            homeTank2.collideWithPlayer(homeTank);
            homeTank.collideWithPlayer(homeTank2);
        }

        for (int i = 0; i < metalWall.size(); i++) {
            MetalWall w = metalWall.get(i);
            homeTank.collideWithWall(w);
            if (Player2) homeTank2.collideWithWall(w);
            w.draw(g);
        }

        for (int i = 0; i < otherWall.size(); i++) {
            CommonWall cw = otherWall.get(i);
            homeTank.collideWithWall(cw);
            if (Player2) homeTank2.collideWithWall(cw);
            cw.draw(g);
        }

        for (int i = 0; i < homeWall.size(); i++) {
            CommonWall w = homeWall.get(i);
            homeTank.collideWithWall(w);
            if (Player2) homeTank2.collideWithWall(w);
            w.draw(g);
        }

        if (!Player2) g.drawString("" + homeTank.getLife(), 650, 70);
        else g.drawString("Player1: " + homeTank.getLife() + "    Player2: " + homeTank2.getLife(), 550, 70);
        g.setFont(f1);
        if (!Player2) {
            if (tanks.size() == 0 && home.isLive() && homeTank.isLive() && lose == false) {
                Font f = g.getFont();
                g.setFont(new Font("Times New Roman", Font.BOLD, 60));
                this.otherWall.clear();
                g.drawString("Congratulations! ", 200, 300);
                g.setFont(f);
                win = true;
            }

            if (homeTank.isLive() == false && win == false) {
                Font f = g.getFont();
                g.setFont(new Font("Times New Roman", Font.BOLD, 40));
                tanks.clear();
                bullets.clear();
                g.drawString("Sorry. You lose!", 200, 300);
                lose = true;
                g.setFont(f);
            }
        } else {
            if (!roundEnded) {
                if (homeTank.isLive() && !homeTank2.isLive()) {
                    p1Won = true;
                    endRound();
                }

                if (homeTank2.isLive() && !homeTank.isLive()) {
                    p2Won = true;
                    endRound();
                }

                if (!manualControls)
                    if (currentTime > MAX_TIME) {
                        g.setFont(new Font("Times New Roman", Font.BOLD, 30));
                        String text = "Time out!";
                        int width = g.getFontMetrics().stringWidth(text);
                        g.drawString(text, (Fram_width - width) / 2, Fram_length / 2);
                        endRound();
                    } else
                        g.drawString(String.format("%.2f", (MAX_TIME - currentTime) / 50.0d), 8, 60);

                g.drawString("p1 score: " + homeTank.getScore(homeTank2), 100, 60);
                g.drawString("p2 score: " + homeTank2.getScore(homeTank), 200, 60);
            }
        }
        g.setColor(c);
        g.setFont(f1);
    }

    private void endRound() {
        roundEnded = true;
        printable = false;
        threadPool.shutdownNow();
        dispose();

        int score1 = this.homeTank.getScore(this.homeTank2);
        int score2 = this.homeTank2.getScore(this.homeTank);

        log.debug("round ended: " + this.getTitle() +
                " (took " + getCurrentDurration() + ") " +
                (this.p1Won ? "player1 won " : "") +
                (this.p2Won ? "player2 won " : "") +
                "p1 score: " + score1 + ", p2 score: " + score2);

        for (GameListener listener : this.gameListeners) {
            listener.roundEnded(this.p1Won, this.p2Won);
        }

        this.gameListeners.clear();

        for (ScoreListener listener : this.scoreListeners) {
            listener.roundEndedWithScore(score1, score2);
        }

        this.scoreListeners.clear();
    }

    public void addListener(GameListener listener) {
        gameListeners.add(listener);
    }

    public void removeListener(GameListener listener) {
        gameListeners.remove(listener);
    }

    public void addScoreListener(ScoreListener listener) {
        scoreListeners.add(listener);
    }

    public void removeScoreListener(ScoreListener listener) {
        scoreListeners.remove(listener);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("NewGame")) {
            printable = false;
            Object[] options = {"Confirm", "Cancel"};
            int response = JOptionPane.showOptionDialog(this, "Confirm to start a new game?", "",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    options, options[0]);
            if (response == 0) {

                printable = true;
                this.dispose();
                TankClient game = new TankClient(getTitle(), manualControls);
                if (isVisible())
                    game.showClient();
            } else {
                printable = true;
                threadPool.execute(new PaintTask(this));
            }

        } else if (e.getActionCommand().endsWith("Stop")) {
            printable = false;
        } else if (e.getActionCommand().equals("Continue")) {

            if (!printable) {
                printable = true;
                threadPool.execute(new PaintTask(this));
            }
        } else if (e.getActionCommand().equals("Exit")) {
            printable = false;
            Object[] options = {"Confirm", "Cancel"};
            int response = JOptionPane.showOptionDialog(this, "Confirm to exit?", "",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    options, options[0]);
            if (response == 0) {
                System.exit(0);
            } else {
                printable = true;
                threadPool.execute(new PaintTask(this));

            }

        } else if (e.getActionCommand().equals("Player2")) {
            printable = false;
            Object[] options = {"Confirm", "Cancel"};
            int response = JOptionPane.showOptionDialog(this, "Confirm to add player2?", "",
                    JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    options, options[0]);
            if (response == 0) {
                printable = true;
                this.dispose();
                TankClient Player2add = new TankClient(getTitle(), manualControls);
                if (isVisible())
                    Player2add.showClient();
                Player2add.Player2 = true;
            } else {
                printable = true;
                threadPool.execute(new PaintTask(this));
            }
        } else if (e.getActionCommand().equals("help")) {
            printable = false;
            JOptionPane.showMessageDialog(null, "Use WSAD to control Player1's direction, use F to fire and restart with pressing R\nUse diection key to Control Player2, use slash to fire",
                    "Help", JOptionPane.INFORMATION_MESSAGE);
            this.setVisible(true);
            printable = true;
            threadPool.execute(new PaintTask(this));
        } else if (e.getActionCommand().equals("level1")) {
            Tank.count = 12;
            Tank.speedX = 6;
            Tank.speedY = 6;
            Bullets.speedX = 10;
            Bullets.speedY = 10;
            this.dispose();
            TankClient game = new TankClient(getTitle(), manualControls);
            if (isVisible())
                game.showClient();
        } else if (e.getActionCommand().equals("level2")) {
            Tank.count = 12;
            Tank.speedX = 10;
            Tank.speedY = 10;
            Bullets.speedX = 12;
            Bullets.speedY = 12;
            this.dispose();
            TankClient game = new TankClient(getTitle(), manualControls);
            if (isVisible())
                game.showClient();

        } else if (e.getActionCommand().equals("level3")) {
            Tank.count = 20;
            Tank.speedX = 14;
            Tank.speedY = 14;
            Bullets.speedX = 16;
            Bullets.speedY = 16;
            this.dispose();
            TankClient game = new TankClient(getTitle(), manualControls);
            if (isVisible())
                game.showClient();
        } else if (e.getActionCommand().equals("level4")) {
            Tank.count = 20;
            Tank.speedX = 16;
            Tank.speedY = 16;
            Bullets.speedX = 18;
            Bullets.speedY = 18;
            this.dispose();
            TankClient game = new TankClient(getTitle(), manualControls);
            if (isVisible())
                game.showClient();
        } else if (e.getActionCommand().equals("Join")) {
            printable = false;
            String s = JOptionPane.showInputDialog("Please input URL:");
            printable = true;
            threadPool.execute(new PaintTask(this));
        }

    }

    public String getCurrentDurration() {
        long duration = System.nanoTime() - upTime;
        return String.format("%.2f sec", duration / 1_000_000_000.0);
    }

    private static final class TankConnection implements GameClientConnection {

        private Tank tank;
        private Context context;

        private TankConnection(TankClient tankClient, Tank tank, Tank enemy) {
            this.tank = Objects.requireNonNull(tank);
            context = new Context(tankClient, tank, enemy);
        }

        @Override
        public void sendOrders(Orders orders) {
            ensureOpen();
            tank.receiveOrders(orders);
        }

        @Override
        public Context getContext() {
            ensureOpen();
            return context;
        }

        @Override
        public void close() {
            tank = null;
            context.close();
        }

        @Override
        public boolean isClosed() {
            return context.isClosed();
        }

        private void ensureOpen() {
            if (context.isClosed())
                throw new IllegalStateException("connection is already closed");
        }
    }

    private static final class PaintTask implements Runnable {

        private TankClient parent;
        private boolean alreadyRun;

        public PaintTask(TankClient parent) {
            this.parent = Objects.requireNonNull(parent);
        }

        public void run() {
            if (alreadyRun)
                throw new IllegalStateException("paint task have been already run");

            alreadyRun = true;
            while (parent.printable && !Thread.interrupted()) {
                parent.currentTime++;
                parent.repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (!parent.roundEnded)
                parent.endRound();

            parent = null;
        }

    }

    private final class KeyMonitor extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            if (isManualControls()) {
                homeTank.keyReleased(e);
                homeTank2.keyReleased(e);
            }
        }

        public void keyPressed(KeyEvent e) {
            if (isManualControls()) {
                homeTank.keyPressed(e);
                homeTank2.keyPressed(e);
            }
        }

    }

}

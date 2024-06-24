package com.jsoftware95.jpanzer.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.Random;

@SuppressWarnings("ALL")
public class Tank {
    public static final Color AIMING_LINE_COLOR = new Color(255, 0, 0, 128);
    public static final int INIT_LIFE = 150;
    public static final int width = 35, length = 35;
    public static final int RELOAD_TIME = 100;
    private static final Random r = new Random();
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final Image[] tankImags;
    public static int speedX = 6, speedY = 6;
    public static int MAX_SPEED_X = speedX, MAX_SPEED_Y = speedY;
    public static int count;

    static {
        tankImags = new Image[] {
                tk.getImage(BombTank.class.getResource("images/tankD.gif")),
                tk.getImage(BombTank.class.getResource("images/tankU.gif")),
                tk.getImage(BombTank.class.getResource("images/tankL.gif")),
                tk.getImage(BombTank.class.getResource("images/tankR.gif")),
                tk.getImage(BombTank.class.getResource("images/HtankD.gif")),
                tk.getImage(BombTank.class.getResource("images/HtankU.gif")),
                tk.getImage(BombTank.class.getResource("images/HtankL.gif")),
                tk.getImage(BombTank.class.getResource("images/HtankR.gif")),
                tk.getImage(BombTank.class.getResource("images/HtankD2.gif")),
                tk.getImage(BombTank.class.getResource("images/HtankU2.gif")),
                tk.getImage(BombTank.class.getResource("images/HtankL2.gif")),
                tk.getImage(BombTank.class.getResource("images/HtankR2.gif")),
        };

    }

    private final boolean good;
    public volatile int score;
    TankClient tc;
    private boolean blockedBefore;
    private volatile Direction direction = Direction.STOP;
    private volatile Direction Kdirection = Direction.U;
    private int player;
    private int x, y;
    private int oldX, oldY;
    private boolean live = true;
    private int life = INIT_LIFE;
    private int rate = 1;
    private int step = r.nextInt(10) + 5;
    private volatile boolean bL, bU, bR, bD;
    private long lastReloadTime = -1;
    private int lastPositionX = -1;
    private int lastPositionY = -1;

    public Tank(final int x, final int y, final boolean good) {

        if (x > (width / 2))
            this.x = x - (width / 2);
        else
            this.x = x;

        if (y > (length / 2))
            this.y = y - (length / 2);
        else
            this.y = y;

        this.oldX = x;
        this.oldY = y;
        this.good = good;
    }

    public Tank(final int x, final int y, final boolean good, final Direction dir, final TankClient tc, final int player) {
        this(x, y, good);
        this.direction = dir;
        this.tc = tc;
        this.player = player;
        if (player == 1)
            Kdirection = Direction.U;

        if (player == 2)
            Kdirection = Direction.D;

    }

    public void draw(final Graphics g) {
        if (!live) {
            if (!good) {
                tc.tanks.remove(this);
            }
            return;
        }
        //if (good)
        //new DrawBloodbBar().draw(g);

        if (lastPositionX < 0)
            lastPositionX = x;

        if (lastPositionY < 0)
            lastPositionY = y;

        int movedY = y - lastPositionY;
        int diff = Math.abs(tc.homeTank.y - tc.homeTank2.y);
        if (diff < 200)
            diff = 0;
        else if (diff < 400)
            diff = (int) (diff * 0.01);
        else
            diff = 6;

        if (this == tc.homeTank) {
            if (tc.homeTank.y > tc.homeTank2.y) {
                if (movedY > 0)
                    score -= movedY * diff;
                else
                    score += Math.abs(movedY) * (blockedBefore ? 3 : 1);
            } else if (tc.homeTank.y < tc.homeTank2.y) {
                if (movedY > 0)
                    score += movedY * (blockedBefore ? 3 : 1);
                else
                    score -= Math.abs(movedY) * diff;
            }
        } else if (this == tc.homeTank2) {
            if (tc.homeTank2.y > tc.homeTank.y) {
                if (movedY > 0)
                    score -= movedY * diff;
                else
                    score += Math.abs(movedY) * (blockedBefore ? 3 : 1);
            } else if (tc.homeTank2.y < tc.homeTank.y) {
                if (movedY > 0)
                    score += movedY * (blockedBefore ? 3 : 1);
                else
                    score -= Math.abs(movedY) * diff;
            }
        }

        lastPositionX = x;
        lastPositionY = y;

        switch (Kdirection) {

            case D:
                if (player == 1) {
                    g.drawImage(tankImags[4], x, y, null);
                } else if (tc.Player2 && player == 2) {
                    g.drawImage(tankImags[8], x, y, null);
                } else {
                    g.drawImage(tankImags[0], x, y, null);
                }
                break;

            case U:
                if (player == 1) {
                    g.drawImage(tankImags[5], x, y, null);
                } else if (tc.Player2 && player == 2) {
                    g.drawImage(tankImags[9], x, y, null);
                } else {
                    g.drawImage(tankImags[1], x, y, null);
                }
                break;
            case L:
                if (player == 1) {
                    g.drawImage(tankImags[6], x, y, null);
                } else if (tc.Player2 && player == 2) {
                    g.drawImage(tankImags[10], x, y, null);
                } else {
                    g.drawImage(tankImags[2], x, y, null);
                }
                break;

            case R:
                if (player == 1) {
                    g.drawImage(tankImags[7], x, y, null);
                } else if (tc.Player2 && player == 2) {
                    g.drawImage(tankImags[11], x, y, null);
                } else {
                    g.drawImage(tankImags[3], x, y, null);
                }
                break;

        }

        move();
    }

    public void drawAimLine(final Graphics g) {
        if (!live) {
            if (!good) {
                tc.tanks.remove(this);
            }
            return;
        }

        Line2D line = null;
        switch (Kdirection) {
            case D:
                line = getAimlineToDown();
                break;
            case U:
                line = getAimlineToUp();
                break;
            case L:
                line = getAimlineToLeft();
                break;
            case R:
                line = getAimlineToRight();
                break;
        }

        if (line != null && isLoaded()) {
            Color oldColor = g.getColor();
            g.setColor(AIMING_LINE_COLOR);
            g.drawLine((int) line.getX1(), (int) line.getY1(), (int) line.getX2(), (int) line.getY2());
            g.setColor(oldColor);
        }
    }

    private Line2D getAimlineToRight() {
        final int gunX = x + length;
        final int gunY = y + width / 2;
        Line2D line = new Line2D.Float(gunX, gunY, gunX + TankClient.Fram_width, gunY);

        if (this != tc.homeTank) {
            if (tc.homeTank.isLive() && line.intersects(tc.homeTank.getRect())) {
                tc.homeTank2.score++;
                line.setLine(line.getX1(), line.getY1(), tc.homeTank.x + Tank.length / 2, line.getY2());
            }
        } else {
            if (tc.homeTank2.isLive() && line.intersects(tc.homeTank2.getRect())) {
                tc.homeTank.score++;
                line.setLine(line.getX1(), line.getY1(), tc.homeTank2.x + Tank.length / 2, line.getY2());
            }
        }

        return line;
    }

    private Line2D getAimlineToLeft() {
        final int gunX = x;
        final int gunY = y + width / 2;
        Line2D line = new Line2D.Float(gunX, gunY, gunX - TankClient.Fram_width, gunY);

        if (this != tc.homeTank) {
            if (tc.homeTank.isLive() && line.intersects(tc.homeTank.getRect())) {
                tc.homeTank2.score++;
                line.setLine(line.getX1(), line.getY1(), tc.homeTank.x + Tank.length / 2, line.getY2());
            }
        } else {
            if (tc.homeTank2.isLive() && line.intersects(tc.homeTank2.getRect())) {
                tc.homeTank.score++;
                line.setLine(line.getX1(), line.getY1(), tc.homeTank2.x + Tank.length / 2, line.getY2());
            }
        }

        return line;
    }

    private Line2D getAimlineToUp() {
        final int gunX = x + width / 2;
        final int gunY = y;
        Line2D line = new Line2D.Float(gunX, gunY, gunX, gunY - TankClient.Fram_length);

        if (this != tc.homeTank) {
            if (tc.homeTank.isLive() && line.intersects(tc.homeTank.getRect())) {
                tc.homeTank2.score++;
                line.setLine(line.getX1(), line.getY1(), line.getX2(), tc.homeTank.y + Tank.length / 2);
            }
        } else {
            if (tc.homeTank2.isLive() && line.intersects(tc.homeTank2.getRect())) {
                tc.homeTank.score++;
                line.setLine(line.getX1(), line.getY1(), line.getX2(), tc.homeTank2.y + Tank.length / 2);
            }
        }

        return line;
    }

    private Line2D getAimlineToDown() {
        final int gunX = x + width / 2;
        final int gunY = y + length;
        Line2D line = new Line2D.Float(gunX, gunY, gunX, gunY + TankClient.Fram_length);

        if (this != tc.homeTank) {
            if (tc.homeTank.isLive() && line.intersects(tc.homeTank.getRect())) {
                tc.homeTank2.score++;
                line.setLine(line.getX1(), line.getY1(), line.getX2(), tc.homeTank.y + Tank.length / 2);
            }
        } else {
            if (tc.homeTank2.isLive() && line.intersects(tc.homeTank2.getRect())) {
                tc.homeTank.score++;
                line.setLine(line.getX1(), line.getY1(), line.getX2(), tc.homeTank2.y + Tank.length / 2);
            }
        }

        return line;
    }

    void move() {

        this.oldX = x;
        this.oldY = y;

        switch (direction) {
            case L:
                x -= speedX;
                break;
            case U:
                y -= speedY;
                break;
            case R:
                x += speedX;
                break;
            case D:
                y += speedY;
                break;
            case STOP:
                break;
        }

        if (this.direction != Direction.STOP) {
            this.Kdirection = this.direction;
        }

        if (x < 0) {
            x = 0;
            score -= 2;
            blockedBefore = true;
        }

        if (y < 40) {
            y = 40;
            score -= 2;
            blockedBefore = true;
        }

        if (x + Tank.width > TankClient.Fram_width) {
            x = TankClient.Fram_width - Tank.width;
            score -= 2;
            blockedBefore = true;
        }

        if (y + Tank.length > TankClient.Fram_length) {
            y = TankClient.Fram_length - Tank.length;
            score -= 2;
            blockedBefore = true;
        }

        if (!good) {
            final Direction[] directons = Direction.values();
            if (step == 0) {
                step = r.nextInt(12) + 3;
                final int mod = r.nextInt(9);
                if (playertankaround()) {
                    if (x == tc.homeTank.x) {
                        if (y > tc.homeTank.y) direction = directons[1];
                        else if (y < tc.homeTank.y) direction = directons[3];
                    } else if (y == tc.homeTank.y) {
                        if (x > tc.homeTank.x) direction = directons[0];
                        else if (x < tc.homeTank.x) direction = directons[2];
                    } else {
                        final int rn = r.nextInt(directons.length);
                        direction = directons[rn];
                    }
                    rate = 2;
                } else if (mod == 1) {
                    rate = 1;
                } else if (1 < mod && mod <= 3) {
                    rate = 1;
                } else {
                    final int rn = r.nextInt(directons.length);
                    direction = directons[rn];
                    rate = 1;
                }
            }
            step--;
            if (rate == 2) {
                if (r.nextInt(40) > 35)
                    this.fire();
            } else if (r.nextInt(40) > 38)
                this.fire();
        }
    }

    public boolean playertankaround() {
        int rx = x - 15, ry = y - 15;
        if ((x - 15) < 0) rx = 0;
        if ((y - 15) < 0) ry = 0;
        final Rectangle a = new Rectangle(rx, ry, 60, 60);
        if (this.live && a.intersects(tc.homeTank.getRect())) {
            return true;
        }
        return false;
    }

    public int getzone(final int x, final int y) {
        final int tempx = x;
        final int tempy = y;
        if (tempx < 85 && tempy < 300) return 11;
        else if (tempx > 85 && tempx < 140 && tempy > 0 && tempy < 100) return 9;
        else if (tempx > 85 && tempx < 140 && tempy > 254 && tempy < 300) return 10;
        else if (tempx > 0 && tempx < 200 && tempy > 300 && tempy < 715) return 12;
        else if (tempx > 140 && tempx < 400 && tempy > 0 && tempy < 150) return 7;
        else if (tempx > 140 && tempx < 400 && tempy > 210 && tempy < 300) return 8;
        else if (tempx > 400 && tempx < 500 && tempy > 0 && tempy < 300) return 6;
        else if (tempx > 500 && tempy > 0 && tempy < 180) return 5;
        else if (tempx > 500 && tempy > 180 && tempy < 300) return 4;
        else if (tempx > 520 && tempx < 600 && tempy > 3000 && tempy < 715) return 2;
        else if (tempx > 600 && tempy > 300 && tempy < 715) return 3;
        return 1;
    }

    public int getdirect(final int a, final int b) {
        if (b == 13) {

        }
        return 4;
    }

    private void changToOldDir() {
        x = oldX;
        y = oldY;
        score--;
    }

    public void receiveOrders(Orders orders) {
        bR = orders.isMoveRight();
        bL = orders.isMoveLeft();
        bU = orders.isMoveUp();
        bD = orders.isMoveDown();
        decideDirection();

        if (orders.isFire())
            fire();
    }

    public void keyPressed(final KeyEvent e) {
        final int key = e.getKeyCode();
        if (player == 1) {
            switch (key) {
                case KeyEvent.VK_R:
                    tc.tanks.clear();
                    tc.bullets.clear();
                    tc.trees.clear();
                    tc.otherWall.clear();
                    tc.homeWall.clear();
                    tc.metalWall.clear();
                    tc.homeTank.setLive(false);
                    if (tc.tanks.size() == 0) {
                        for (int i = 0; i < 20; i++) {
                            if (i < 9)
                                tc.tanks.add(new Tank(150 + 70 * i, 40, false,
                                        Direction.R, tc, 0));
                            else if (i < 15)
                                tc.tanks.add(new Tank(700, 140 + 50 * (i - 6), false,
                                        Direction.D, tc, 0));
                            else
                                tc.tanks.add(new Tank(10, 50 * (i - 12), false,
                                        Direction.L, tc, 0));
                        }
                    }

                    tc.homeTank = new Tank(300, 560, true, Direction.STOP, tc, 0);
                    if (!tc.home.isLive())
                        tc.home.setLive(true);
                    final TankClient abc = new TankClient(tc.getTitle(), tc.isManualControls());
                    if (tc.isVisible())
                        abc.showClient();
                    if (tc.Player2) abc.Player2 = true;
                    break;
                case KeyEvent.VK_D:
                    bR = true;
                    break;

                case KeyEvent.VK_A:
                    bL = true;
                    break;

                case KeyEvent.VK_W:
                    bU = true;
                    break;

                case KeyEvent.VK_S:
                    bD = true;
                    break;
            }
        }
        if (player == 2) {
            switch (key) {
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    break;

                case KeyEvent.VK_LEFT:
                    bL = true;
                    break;

                case KeyEvent.VK_UP:
                    bU = true;
                    break;

                case KeyEvent.VK_DOWN:
                    bD = true;
                    break;
            }
        }
        decideDirection();
    }

    void decideDirection() {
        if (!bL && !bU && bR && !bD)
            direction = Direction.R;

        else if (bL && !bU && !bR && !bD)
            direction = Direction.L;

        else if (!bL && bU && !bR && !bD)
            direction = Direction.U;

        else if (!bL && !bU && !bR && bD)
            direction = Direction.D;

        else if (!bL && !bU && !bR && !bD)
            direction = Direction.STOP;
        else
            score -= 2;
    }

    public void keyReleased(final KeyEvent e) {
        final int key = e.getKeyCode();
        if (player == 1) {
            switch (key) {

                case KeyEvent.VK_F:
                    fire();
                    break;

                case KeyEvent.VK_D:
                    bR = false;
                    break;

                case KeyEvent.VK_A:
                    bL = false;
                    break;

                case KeyEvent.VK_W:
                    bU = false;
                    break;

                case KeyEvent.VK_S:
                    bD = false;
                    break;

            }
        }
        if (player == 2) {
            switch (key) {

                case KeyEvent.VK_SPACE:
                    fire();
                    break;

                case KeyEvent.VK_RIGHT:
                    bR = false;
                    break;

                case KeyEvent.VK_LEFT:
                    bL = false;
                    break;

                case KeyEvent.VK_UP:
                    bU = false;
                    break;

                case KeyEvent.VK_DOWN:
                    bD = false;
                    break;

            }
        }
        decideDirection();
    }

    public boolean isLoaded() {
        return tc.currentTime >= lastReloadTime;
    }

    public Bullets fire() {
        if (!live || !isLoaded())
            return null;
        final int x = this.x + Tank.width / 2 - Bullets.width / 2;
        final int y = this.y + Tank.length / 2 - Bullets.length / 2;
        final Bullets m = new Bullets(x, y + 2, good, Kdirection, this.tc, this);
        tc.bullets.add(m);
        lastReloadTime = tc.currentTime + RELOAD_TIME;
        score--;
        return m;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, length);
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(final boolean live) {
        this.live = live;
    }

    public boolean isGood() {
        return good;
    }

    public boolean collideWithWall(final CommonWall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.changToOldDir();
            return true;
        }
        return false;
    }

    public boolean collideWithWall(final MetalWall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.changToOldDir();
            return true;
        }
        return false;
    }

    public boolean collideRiver(final River r) {
        if (this.live && this.getRect().intersects(r.getRect())) {
            this.changToOldDir();
            return true;
        }
        return false;
    }

    public boolean collideHome(final Home h) {
        if (this.live && this.getRect().intersects(h.getRect())) {
            this.changToOldDir();
            return true;
        }
        return false;
    }

    public boolean collideWithTanks(final java.util.List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            final Tank t = tanks.get(i);
            if (this != t) {
                if (this.live && t.isLive()
                        && this.getRect().intersects(t.getRect())) {
                    this.changToOldDir();
                    t.changToOldDir();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collideWithPlayer(Tank tank) {
        if (this != tank && live && tank.live && this.getRect().intersects(tank.getRect())) {
            this.changToOldDir();
            tank.changToOldDir();
            return true;
        }
        return false;
    }

    public int getLife() {
        return life;
    }

    public void setLife(final int life) {
        this.life = life;
    }

    public boolean eat(final GetBlood b) {
        if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
            if (this.life <= 100)
                this.life = this.life + 100;
            else
                this.life = 200;
            b.setLive(false);
            return true;
        }
        return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction getDirection() {
        return direction;
    }

    public Direction getKdirection() {
        return Kdirection;
    }

    public int getCurrentSpeedX() {
        if (lastPositionX < 0 || x < 0)
            return 0;
        else
            return x - lastPositionX;
    }

    public int getCurrentSpeedY() {
        if (lastPositionY < 0 || y < 0)
            return 0;
        else
            return y - lastPositionY;
    }

    public int hitPointScore() {
        return (Tank.INIT_LIFE - this.getLife()) * 10;
    }

    public int getScore(Tank homeTank2) {
        return score + homeTank2.hitPointScore();
    }

    private class DrawBloodbBar {
        public void draw(final Graphics g) {
            final Color c = g.getColor();
            g.setColor(Color.RED);
            g.drawRect(375, 585, width, 10);
            final int w = width * life / 200;
            g.fillRect(375, 585, w, 10);
            g.setColor(c);
        }
    }
}
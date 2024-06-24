package com.jsoftware95.jpanzer.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"ClassWithTooManyFields", "StaticNonFinalField", "InstanceVariableOfConcreteClass", "MethodParameterOfConcreteClass", "FeatureEnvy", "OverlyComplexBooleanExpression", "UnusedReturnValue", "Duplicates"})
public class Bullets {
    public static final int width = 10;
    public static final int length = 10;
    public static final int INIT_SPEED = 36;
    public static final int HP_PER_HIT = 50;
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final Image[] bulletImages;
    private static final Map<String, Image> imgs = new HashMap<>();
    public static int speedX = INIT_SPEED;
    public static int speedY = INIT_SPEED;

    static {
        bulletImages = new Image[]{
                tk.getImage(Bullets.class.getResource(
                        "images/bulletL.gif")),

                tk.getImage(Bullets.class.getResource(
                        "images/bulletU.gif")),

                tk.getImage(Bullets.class.getResource(
                        "images/bulletR.gif")),

                tk.getImage(Bullets.class.getResource(
                        "images/bulletD.gif")),

        };

        imgs.put("L", bulletImages[0]);

        imgs.put("U", bulletImages[1]);

        imgs.put("R", bulletImages[2]);

        imgs.put("D", bulletImages[3]);

    }

    Direction diretion;
    private int x, y;
    private boolean good;
    private boolean live = true;
    private Tank owner;
    private TankClient tc;

    public Bullets(final int x, final int y, final Direction dir, Tank owner) {
        this.x = x;
        this.y = y;
        this.diretion = dir;
        this.owner = owner;
    }

    public Bullets(final int x, final int y, final boolean good, final Direction dir, final TankClient tc, Tank owner) {
        this(x, y, dir, owner);
        this.good = good;
        this.tc = tc;
    }

    private void move() {

        switch (diretion) {
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

        if (x < 0 || y < 0 || x > TankClient.Fram_width
                || y > TankClient.Fram_length) {
            live = false;
        }
    }

    public void draw(final Graphics g) {
        if (!live) {
            tc.bullets.remove(this);
            return;
        }

        switch (diretion) {
            case L:
                g.drawImage(imgs.get("L"), x, y, null);
                break;

            case U:
                g.drawImage(imgs.get("U"), x, y, null);
                break;

            case R:
                g.drawImage(imgs.get("R"), x, y, null);
                break;

            case D:
                g.drawImage(imgs.get("D"), x, y, null);
                break;

            default:
                assert false;
        }

        move();
    }

    public boolean isLive() {
        return live;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, length);
    }

    public void hitTanks(final Iterable<Tank> tanks) {
        for (final Tank tank : tanks) {
            if (hitTank(tank)) {
                return;
            }
        }
    }

    public boolean hitTank(final Tank t) {

        if (this.owner != t && this.live && this.getRect().intersects(t.getRect()) && t.isLive()) {

            final BombTank e = new BombTank(t.getX(), t.getY(), tc);
            tc.bombTanks.add(e);

            t.setLife(t.getLife() - HP_PER_HIT);
            if (t.getLife() <= 0)
                t.setLive(false);

            this.live = false;

            return true;
        }
        return false;
    }

    public boolean hitWall(final CommonWall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.live = false;
            this.tc.otherWall.remove(w);
            this.tc.homeWall.remove(w);
            return true;
        }
        return false;
    }

    public boolean hitBullet(final Bullets w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.live = false;
            this.tc.bullets.remove(w);
            return true;
        }
        return false;
    }

    public boolean hitWall(final MetalWall w) {
        if (this.live && this.getRect().intersects(w.getRect())) {
            this.live = false;
            return true;
        }
        return false;
    }

    public boolean hitHome() {
        if (this.live && this.getRect().intersects(tc.home.getRect())) {
            this.live = false;
            this.tc.home.setLive(false);
            return true;
        }
        return false;
    }

}

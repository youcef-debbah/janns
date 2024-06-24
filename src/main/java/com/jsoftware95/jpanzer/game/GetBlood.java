package com.jsoftware95.jpanzer.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

@SuppressWarnings("InstanceVariableOfConcreteClass")
public class GetBlood {

    public static final int width = 34;
    public static final int length = 30;
    public static final int BOUND = 98;
    private static final Random r = new Random();
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final Image[] bloodImages;

    static {
        bloodImages = new Image[]{tk.getImage(CommonWall.class
                .getResource("images/hp.png")),};
    }

    private final int[][] position = {{700, 196}, {500, 58}, {80, 300},
            {600, 321}, {345, 456}, {123, 321}, {258, 413}};
    TankClient tc;
    int step;
    private int x, y;
    private boolean live;

    public void draw(final Graphics g) {
        if (r.nextInt(100) > BOUND) {
            this.live = true;
            move();
        }
        if (!live)
            return;
        g.drawImage(bloodImages[0], x, y, null);

    }

    private void move() {
        step++;
        if (step == position.length) {
            step = 0;
        }
        x = position[step][0];
        y = position[step][1];
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

}

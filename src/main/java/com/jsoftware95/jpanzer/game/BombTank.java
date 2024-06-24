package com.jsoftware95.jpanzer.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

@SuppressWarnings({"InstanceVariableOfConcreteClass", "MethodParameterOfConcreteClass"})
public class BombTank {
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final Image[] imgs = {
            tk.getImage(BombTank.class.getResource(
                    "images/1.gif")),
            tk.getImage(BombTank.class.getResource(
                    "images/2.gif")),
            tk.getImage(BombTank.class.getResource(
                    "images/3.gif")),
            tk.getImage(BombTank.class.getResource(
                    "images/4.gif")),
            tk.getImage(BombTank.class.getResource(
                    "images/5.gif")),
            tk.getImage(BombTank.class.getResource(
                    "images/6.gif")),
            tk.getImage(BombTank.class.getResource(
                    "images/7.gif")),
            tk.getImage(BombTank.class.getResource(
                    "images/8.gif")),
            tk.getImage(BombTank.class.getResource(
                    "images/9.gif")),
            tk.getImage(BombTank.class.getResource(
                    "images/10.gif")),};
    private final int x;
    private final int y;
    private final TankClient tc;
    int step;
    private boolean live = true;

    public BombTank(final int x, final int y, final TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(final Graphics g) {

        if (!live) {
            tc.bombTanks.remove(this);
            return;
        }
        if (step == imgs.length) {
            live = false;
            step = 0;
            return;
        }

        g.drawImage(imgs[step], x, y, null);
        step++;
    }
}

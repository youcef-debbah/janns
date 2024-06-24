package com.jsoftware95.jpanzer.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class CommonWall {
    public static final int width = 22;
    public static final int length = 21;
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final Image[] wallImages;

    static {
        wallImages = new Image[]{
                tk.getImage(CommonWall.class.getResource("images/commonWall.gif")),};
    }

    int x, y;
    TankClient tc;

    public CommonWall(final int x, final int y, final TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(final Graphics g) {
        g.drawImage(wallImages[0], x, y, null);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, length);
    }
}

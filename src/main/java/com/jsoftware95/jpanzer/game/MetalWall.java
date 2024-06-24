package com.jsoftware95.jpanzer.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

@SuppressWarnings({"InstanceVariableOfConcreteClass", "MethodParameterOfConcreteClass"})
public class MetalWall {
    public static final int width = 36;
    public static final int length = 37;
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final Image[] wallImages;

    static {
        wallImages = new Image[]{tk.getImage(CommonWall.class
                .getResource("images/metalWall.gif")),};
    }

    private final int x, y;
    TankClient tc;

    public MetalWall(final int x, final int y, final TankClient tc) {
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

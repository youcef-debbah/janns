package com.jsoftware95.jpanzer.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

@SuppressWarnings({"InstanceVariableOfConcreteClass", "MethodParameterOfConcreteClass"})
public class River {
    public static final int imageWidth = 195;
    public static final int imageLength = 161;
    public static final int riverWidth = imageWidth - 60;
    public static final int riverLength = TankClient.Fram_length;
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static final Image[] riverImages;

    static {
        final Image[] data = {
                tk.getImage(CommonWall.class.getResource("images/river0.png")),
                tk.getImage(CommonWall.class.getResource("images/river1.png")),
                tk.getImage(CommonWall.class.getResource("images/river2.png")),
                tk.getImage(CommonWall.class.getResource("images/river3.png")),
                tk.getImage(CommonWall.class.getResource("images/river4.png")),
        };

        riverImages = new Image[]{data[0],
                data[1], data[2], data[3],
                data[4], data[4], data[4],
                data[3], data[2], data[1],
                data[0], data[0]};
    }

    TankClient tc;
    private int x, y;
    private volatile int currentTime;

    public River(final int x, final int y, final TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public static int getRiverWidth() {
        return riverWidth;
    }

    public static int getRiverLength() {
        return riverLength;
    }

    public void draw(final Graphics g) {
        final int frame = (currentTime / 8) % riverImages.length;
        currentTime++;
        final Image image = riverImages[frame];
        final int newX = x - (imageWidth - riverWidth) / 2;
        int length = 0;
        while (length < riverLength) {
            g.drawImage(image, newX, y + length, null);
            length += imageLength;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, riverWidth, riverLength);
    }

}

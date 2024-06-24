package com.jsoftware95.jpanzer.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

@SuppressWarnings({"InstanceVariableOfConcreteClass", "MethodParameterOfConcreteClass"})
public class Home {
    public static final int FONT_SIZE = 40;
    public static final int width = 43, length = 43;
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] homeImages;

    static {
        homeImages = new Image[]{tk.getImage(CommonWall.class
                .getResource("images/home.jpg")),};
    }

    private final int x, y;
    private final TankClient tc;
    private boolean live = true;

    public Home(final int x, final int y, final TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void gameOver(final Graphics g) {

        tc.tanks.clear();
        tc.metalWall.clear();
        tc.otherWall.clear();
        tc.bombTanks.clear();
        tc.theRiver.clear();
        tc.trees.clear();
        tc.bullets.clear();
        tc.homeTank.setLive(false);
        final Color color = g.getColor();
        g.setColor(Color.green);
        final Font font = g.getFont();
        g.setFont(new Font(" ", Font.PLAIN, FONT_SIZE));
        g.setFont(font);
        g.setColor(color);

    }

    public void draw(final Graphics g) {

        if (live) {
            g.drawImage(homeImages[0], x, y, null);

            for (int i = 0; i < tc.homeWall.size(); i++) {
                final CommonWall w = tc.homeWall.get(i);
                w.draw(g);
            }
        } else {
            gameOver(g);

        }
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(final boolean live) {
        this.live = live;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, length);
    }

}

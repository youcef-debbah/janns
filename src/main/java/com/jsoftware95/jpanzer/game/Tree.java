package com.jsoftware95.jpanzer.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

@SuppressWarnings({"InstanceVariableOfConcreteClass", "MethodParameterOfConcreteClass"})
public class Tree {
    public static final int width = 30;
    public static final int length = 30;
    private static final Toolkit tk = Toolkit.getDefaultToolkit();
    private static Image[] treeImages;

    static {
        treeImages = new Image[]{
                tk.getImage(CommonWall.class.getResource("images/tree.gif")),
        };
    }

    int x, y;
    TankClient tc;

    public Tree(final int x, final int y, final TankClient tc) {
        this.x = x;
        this.y = y;
        this.tc = tc;
    }

    public void draw(final Graphics g) {
        g.drawImage(treeImages[0], x, y, null);
    }

}

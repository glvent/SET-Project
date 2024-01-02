package org.example.ui.main.game.gameObjects;

import org.example.ui.main.GamePanel;

import java.awt.*;

public abstract class GameObject {
    protected Rectangle bounds;
    protected GamePanel gp;

    public GameObject(int x, int y, GamePanel gp) {
        this.gp = gp;
        this.bounds = new Rectangle(x, y, 0, 0);
    }

    public abstract void render(Graphics2D g2);

    public Rectangle getBounds() {
        return bounds;
    }

}

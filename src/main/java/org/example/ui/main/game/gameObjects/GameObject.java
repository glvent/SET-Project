package org.example.ui.main.gameObjects.objects;

import org.example.ui.main.GamePanel;

import java.awt.*;

public abstract class GameObject {
    protected Rectangle bounds;
    protected ObjectType props;
    protected GamePanel gp;

    public GameObject(int x, int y, GamePanel gp) {
        this.bounds = new Rectangle(x, y, 0, 0);
        this.props = null;
        this.gp = gp;
    }

    public void render(Graphics2D g2) {
        g2.drawImage(props.getImage(), bounds.getBounds().x, bounds.getBounds().y, null);
        g2.setColor(Color.GREEN);
        g2.drawRect(bounds.getBounds().x, bounds.getBounds().y, bounds.width, bounds.height);
        g2.setColor(Color.WHITE);
    }

    public ObjectType getProps() {
        return props;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

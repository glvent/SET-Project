package org.example.ui.gameObjects;

import org.example.ui.utils.Position;
import org.example.ui.utils.Vector;

import java.awt.*;

public class Entity {
    public Position position; // public so I can easily change access without having to create multiple Position objects
    public Vector vector;
    protected final boolean isEnemy; // might change to be able to have enemies turn to friendly
    protected final boolean isCollidable;
    protected final Image image;


    public Entity(Position position, Vector vector, boolean isEnemy, boolean isCollidable, Image image) {
        this.position = position;
        this.vector = vector;
        this.isEnemy = isEnemy;
        this.isCollidable = isCollidable;
        this.image = image;
    }

    public void update() {

    }

    public boolean isCollidable() {
        return isCollidable;
    }

    public boolean isEnemy() {
        return isEnemy;
    }

    public Image getImage() {
        return image;
    }
}

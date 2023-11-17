package org.example.ui.gameObjects;

import org.example.ui.utils.Position;
import org.example.ui.utils.Vector;

import java.awt.*;

public class Enemy extends Entity {
    private static final int UPDATE_INTERVAL = 10;
    private int updateCounter = 0;

    public Enemy(Position position, Vector vector, boolean isEnemy, boolean isCollidable, Image image) {
        super(position, vector, isEnemy, isCollidable, image);
    }

    // noise based enemy spawn?


    public void update(Position playerPosition) {
        if (updateCounter++ > UPDATE_INTERVAL) {
            updateCounter = 0;

            double dx = playerPosition.x - position.x;
            double dy = playerPosition.y - position.y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                dx /= distance;
                dy /= distance;
            }

            position.x += dx * vector.veloX;
            position.y += dy * vector.veloY;
        }
    }
}

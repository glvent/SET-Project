package org.example.ui.main.gameObjects.entities;

import org.example.ui.main.GamePanel;
import org.example.ui.utils.Position;
import org.example.ui.utils.Vector;

import java.awt.*;
import java.util.ArrayList;

public class Enemy extends Entity {
    private static final int UPDATE_INTERVAL = 0; // greater update intervals will make the enemy become choppy
    private int updateCounter = 0;

    public Enemy(Rectangle position, Vector vector, GamePanel gp) {
        super(position, vector, gp);

        MAX_HEALTH = 100;
        health = 100;
        isEnemy = true;
        image = null;
        isCollidable = true;
    }

    @Override
    public void update() {
        moveEnemy();
    }

    private void moveEnemy() {
        Friendly target = findNearestFriendly();
        if (target != null) {
            double dx = target.bounds.x - bounds.x;
            double dy = target.bounds.y - bounds.y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 20 && distance < 100) {
                dx = (dx / distance) * vector.veloX;
                dy = (dy / distance) * vector.veloY;
            }

            moveIfPossible((int) dx, (int) dy);
        }
    }

    private Friendly findNearestFriendly() {
        ArrayList<Friendly> friendlies = getFriendlies();
        Friendly nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Friendly friendly : friendlies) {
            double distance = Position.getDistance(this.bounds, friendly.bounds);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = friendly;
            }
        }

        return nearest;
    }

    @Override
    public void attack() {

    }

    // noise based enemy spawn?

}

package org.example.ui.main.gameObjects.entities;

import org.example.ui.main.GamePanel;
import org.example.ui.utils.Position;
import org.example.ui.utils.Vector;

import java.awt.*;
import java.util.ArrayList;


public class Friendly extends Entity {
    private int attackTimer = 0;
    private static final int ATTACK_INTERVAL = 60;


    public Friendly(Rectangle position, Vector vector, GamePanel gp) {
        super(position, vector, gp);

        MAX_HEALTH = 100;
        health = MAX_HEALTH;
        isEnemy = false;
        image = null;
        isCollidable = true;
    }

    @Override
    public void update() {
      handleMovement();
      handleAttack();
    }

    @Override
    public void attack() {
        Enemy nearestEnemy = findNearestEnemy();
        if (nearestEnemy != null && Position.getDistance(bounds, nearestEnemy.bounds) < 10) {

        }
    }


    private void handleAttack() {
        attackTimer++;
        if (attackTimer >= ATTACK_INTERVAL) {
            attack();
            attackTimer = 0;
        }
    }

    private void handleMovement() {
        double dx;
        double dy;
        double distance;

        if (gp.mouseH.cameraRelativeClick != null) {
            dx = gp.mouseH.cameraRelativeClick.x - bounds.x;
            dy = gp.mouseH.cameraRelativeClick.y - bounds.y;
            distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > 0) {
                dx = (dx / distance) * vector.veloX;
                dy = (dy / distance) * vector.veloY;
            }

            moveIfPossible((int) dx, (int) dy);

            if (bounds.contains(gp.mouseH.guiRelativeClick)) {
                gp.mouseH.cameraRelativeClick = null;
            }
        } else {
            Enemy target = findNearestEnemy();
            if (target != null) {
                dx = target.bounds.x - bounds.x;
                dy = target.bounds.y - bounds.y;
                distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > 0 && distance < 100) {
                    dx = (dx / distance) * vector.veloX;
                    dy = (dy / distance) * vector.veloY;
                    moveIfPossible((int) dx, (int) dy);
                }
            }
        }
    }


    private Enemy findNearestEnemy() {
        ArrayList<Enemy> enemies = getEnemies();
        Enemy nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Enemy enemy : enemies) {
            double distance = Position.getDistance(this.bounds, enemy.bounds);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = enemy;
            }
        }

        return nearest;
    }
}

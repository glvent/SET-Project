package org.example.ui.main.game.entities;

import org.example.ui.main.GamePanel;
import java.awt.*;
import java.util.ArrayList;

public class Friendly extends Entity {
    private int attackTimer = 0;
    private static final int ATTACK_INTERVAL = 60;
    private boolean isFollowingPath = false;

    public Friendly(Rectangle bounds, GamePanel gp) {
        super(bounds, gp);
        vector = new Vector(2, 2);
        MAX_HEALTH = 100;
        health = MAX_HEALTH;
        isEnemy = false;
        image = null;
    }

    @Override
    public void update() {
        if (isFollowingPath) {
            followPath();
        } else {
            handleMovement();
        }
        handleAttack();
    }

    @Override
    public void attack() {
        Enemy nearestEnemy = findNearestEnemy();
        if (nearestEnemy != null && Position.getDistance(bounds, nearestEnemy.bounds) < 10) {
            // Attack logic here
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
        if (gp.mouseH.cameraRelativeClick != null) {
            findPath(gp.mouseH.cameraRelativeClick.x, gp.mouseH.cameraRelativeClick.y);
            gp.gameMap.exploreTilesAround(bounds.x, bounds.y, 5);
            isFollowingPath = true;
            gp.mouseH.resetClick();
        }
    }

    @Override
    protected void followPath() {
        super.followPath();
        if (path == null || currentStep >= path.getLength()) {
            isFollowingPath = false;
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

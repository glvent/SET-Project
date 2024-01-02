package org.example.ui.main.game.entities;

import org.example.ui.main.GamePanel;
import org.example.ui.utils.Vector;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity {
    public Rectangle bounds; // public so I can easily change access without having to create multiple Position objects
    public Vector vector;
    protected boolean isEnemy; // might change to be able to have enemies turn to friendly
    protected boolean isCollidable;
    protected Image image;
    protected final GamePanel gp;
    public static int MAX_HEALTH;
    public int health;


    public Entity(Rectangle bounds, Vector vector, GamePanel gp) {
        this.bounds = bounds;
        this.vector = vector;
        this.gp = gp;
    }

    public void render(Graphics2D g2) {
        drawEntity(g2);
        drawHealthBarForEntity(g2);
    }

    public abstract void update();

    public abstract void attack();

    public Rectangle getBounds() {
        // bounds
        int width = 20;
        int height = 20;
        return new Rectangle(bounds.x, bounds.y, width, height);
    }

    protected boolean checkCollision(int newX, int newY) {
        // new proposed bounds
        Rectangle entityBounds = new Rectangle(newX, newY, getBounds().width, getBounds().height);

        ArrayList<Entity> entities = gp.getEntities();
        for (Entity entity : entities) {
            if (entity != this && entity.isCollidable() && entityBounds.intersects(entity.getBounds())) {
                return true;
            }
        }
        return false;
    }

    protected void moveIfPossible(int dx, int dy) {
        int newX = bounds.x + dx;
        int newY = bounds.y + dy;

        if (!checkCollision(newX, newY)) {
            bounds.x = newX;
            bounds.y = newY;
        }
    }

    private void drawEntity(Graphics2D g2) {
        g2.setColor(this instanceof Friendly ? Color.WHITE : Color.RED);
        g2.fillRect(this.bounds.x, this.bounds.y, 20, 20);
    }

    private void drawHealthBarForEntity(Graphics2D g2) {
        final int healthBarWidth = 40;
        final int healthBarHeight = 5;
        final int yOffset = -10;

        double healthPercentage = (double) this.health / Entity.MAX_HEALTH;
        int currentHealthWidth = (int) (healthBarWidth * healthPercentage);

        int healthBarX = this.bounds.x + (this.bounds.width - healthBarWidth) / 2;
        int healthBarY = this.bounds.y + yOffset;

        g2.setColor(Color.RED);
        g2.fillRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);

        g2.setColor(Color.GREEN);
        g2.fillRect(healthBarX, healthBarY, currentHealthWidth, healthBarHeight);

        g2.setColor(Color.WHITE);
    }

    protected ArrayList<Friendly> getFriendlies() {
        ArrayList<Friendly> friendlies = new ArrayList<>();
        gp.getEntities().forEach(entity -> {
            if (entity instanceof Friendly) {
                friendlies.add((Friendly) entity);
            }
        });
        return friendlies;
    }

    protected ArrayList<Enemy> getEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        gp.getEntities().forEach(entity -> {
            if (entity instanceof Enemy) {
                enemies.add((Enemy) entity);
            }
        });
        return enemies;
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

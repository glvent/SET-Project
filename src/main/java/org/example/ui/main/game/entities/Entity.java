package org.example.ui.main.game.entities;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.GameObject;
import org.example.ui.main.map.GameMap;
import org.example.ui.main.map.TileType;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity {
    protected Rectangle bounds; // needs to be set with image values
    protected Vector vector; // probably should be static
    protected boolean isEnemy; // **
    protected Image image;
    protected final GamePanel gp;
    protected static int MAX_HEALTH; // **
    protected int health; // **

    protected static final int MAX_SEARCH_DISTANCE = 200; // performance impact!
    protected static final boolean ALLOW_DIAG_MOVEMENT = false; // can be glitchy
    protected final AStarPathFinder pathfinder;
    protected Path path;
    protected int currentStep;

    public Entity(Rectangle bounds, GamePanel gp) {
        this.bounds = bounds;
        this.gp = gp;
        this.pathfinder = new AStarPathFinder(new TileBasedMap() {
            @Override
            public boolean blocked(PathFindingContext context, int tx, int ty) {
                return !gp.gameMap.isWalkable(tx, ty);
            }

            @Override
            public float getCost(PathFindingContext context, int tx, int ty) {
                return 1;
            }

            @Override
            public int getWidthInTiles() {
                return gp.gameMap.MAP_WIDTH;
            }

            @Override
            public int getHeightInTiles() {
                return gp.gameMap.MAP_HEIGHT;
            }

            @Override
            public void pathFinderVisited(int x, int y) {
            }
        }, MAX_SEARCH_DISTANCE, ALLOW_DIAG_MOVEMENT);
    }

    public void render(Graphics2D g2) {
        renderEntity(g2);
        drawHealthBarForEntity(g2);
    }

    public abstract void update();

    public abstract void attack();

    protected boolean checkCollision(int newX, int newY) {
        Rectangle newBounds = new Rectangle(newX, newY, bounds.width, bounds.height);

        for (Entity entity : gp.getEntities()) {
            if (this != entity && newBounds.intersects(entity.getBounds())) {
                return true;
            }
        }

        for (GameObject gameObject : gp.gameMap.getGameObjects()) {
            if (newBounds.intersects(gameObject.getBounds())) {
                return true;
            }
        }

        return false;
    }

    protected void moveIfPossible(int dx, int dy) {
        int newX = bounds.x + dx;
        int newY = bounds.y + dy;

        int tileX = newX / GameMap.TILE_SIZE;
        int tileY = newY / GameMap.TILE_SIZE;

        if (gp.gameMap.getTile(tileX, tileY).getProps() == TileType.GRASS && !checkCollision(newX, newY)) {
            bounds.x = newX;
            bounds.y = newY;
        }
    }

    public void findPath(int targetX, int targetY) {
        int startX = bounds.x / GameMap.TILE_SIZE;
        int startY = bounds.y / GameMap.TILE_SIZE;
        int endX = targetX / GameMap.TILE_SIZE;
        int endY = targetY / GameMap.TILE_SIZE;
        path = pathfinder.findPath(null, startX, startY, endX, endY);
        currentStep = 0;
    }

    protected void followPath() {
        if (path != null && currentStep < path.getLength()) {
            Path.Step step = path.getStep(currentStep);
            int targetX = step.getX() * GameMap.TILE_SIZE;
            int targetY = step.getY() * GameMap.TILE_SIZE;

            int speed = 1;
            int dx = targetX - bounds.x;
            int dy = targetY - bounds.y;

            dx = Math.min(speed, Math.max(-speed, dx));
            dy = Math.min(speed, Math.max(-speed, dy));

            moveIfPossible(dx, dy);

            if (Math.abs(bounds.x - targetX) <= speed && Math.abs(bounds.y - targetY) <= speed) {
                bounds.x = targetX;
                bounds.y = targetY;
                currentStep++;

                gp.gameMap.exploreTilesAround(bounds.x / GameMap.TILE_SIZE, bounds.y / GameMap.TILE_SIZE, 5);
            }
        }
    }

    private void renderEntity(Graphics2D g2) {
        g2.setColor(this instanceof Friendly ? Color.WHITE : Color.RED);
        g2.fillRect(bounds.x, bounds.y, 20, 20);

        drawHealthBarForEntity(g2);
    }

    private void drawHealthBarForEntity(Graphics2D g2) {
        final int healthBarWidth = 40;
        final int healthBarHeight = 5;
        final int yOffset = -10;

        double healthPercentage = (double) health / MAX_HEALTH;
        int currentHealthWidth = (int) (healthBarWidth * healthPercentage);

        int healthBarX = bounds.x + (bounds.width - healthBarWidth) / 2;
        int healthBarY = bounds.y + yOffset;

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

    public Rectangle getBounds() {
        return bounds;
    }
}

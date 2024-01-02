package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;

import java.awt.*;

public abstract class Generator extends Building {
    protected int resourcePerHour;
    protected long lastUpdateTime;
    protected double accumulatedTime;

    public Generator(int x, int y, GamePanel gp) {
        super(x, y, gp);

        LEVEL_CAP = 4;
        lastUpdateTime = System.currentTimeMillis();
    }

    public void generate() {
        System.out.println("Generating");
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastUpdateTime; // time in ms
        accumulatedTime += timeElapsed;

        // time to produce one resource unit
        double timeForOneResource = 3600000.0 / resourcePerHour;

        if (accumulatedTime >= timeForOneResource) {
            gp.resourceInventory.addResource(getResourceType(), 1);
            accumulatedTime -= timeForOneResource; // reset or reduce accumulated time
        }

        lastUpdateTime = currentTime;
    }

    public void renderProgressBar(Graphics2D g2) {
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = 10;

        double progress = getProgress();

        g2.setColor(Color.GRAY);
        g2.fillRect(x, y - height - 5, width, height);

        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - height - 5, (int) (width * progress), height);
    }

    public double getProgress() {
        double timeForOneResource = 3600000.0 / resourcePerHour;
        return Math.min(accumulatedTime / timeForOneResource, 1.0); // progress percentage
    }

    @Override
    protected void renderBuildingGameAdditions(Graphics2D g2) {
        renderProgressBar(g2);
    }

    @Override
    protected void renderBuildingModalAdditions(Graphics2D g2) {}

    protected abstract ResourceType getResourceType();
}

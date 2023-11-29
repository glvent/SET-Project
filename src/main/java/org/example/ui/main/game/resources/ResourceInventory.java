package org.example.ui.main.gameObjects.resources;

import org.example.ui.main.GamePanel;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class ResourceInventory {
    private Map<ResourceType, Integer> resources;
    private GamePanel gp;

    public ResourceInventory(GamePanel gp) {
        resources = new EnumMap<>(ResourceType.class);
        this.gp = gp;

        for (ResourceType type : ResourceType.values()) {
            resources.put(type, 0);
        }
    }

    public void addResource(ResourceType type, int amt) {
        // inefficient; refactor!
        resources.put(type, resources.get(type) + amt);
    }

    public void consumeResource(ResourceType type, int amt) {
        resources.put(type, resources.get(type) - amt);
    }

    public int getResourceAmt(ResourceType type) {
        return resources.get(type);
    }

    public void render(Graphics2D g2) {
        final int BAR_WIDTH = 200;
        final int BAR_HEIGHT = 20;
        final int MARGIN = 20;

        int x = gp.getWidth() - BAR_WIDTH - MARGIN;
        int y = BAR_HEIGHT;

        for (ResourceType type : ResourceType.values()) {
            int resourceAmt = getResourceAmt(type);
            // replace 100 with storageBuilding.resourceCap.get(type);
            int filledWidth = (int) (((double) resourceAmt / 100) * BAR_WIDTH);

            g2.setColor(new Color(20, 20, 20, 50));
            g2.fillRoundRect(x, y, BAR_WIDTH, BAR_HEIGHT, 10, 10);

            g2.setColor(getResourceColor(type));
            g2.fillRoundRect(x, y, filledWidth, BAR_HEIGHT, 10, 10);

            g2.setColor(Color.WHITE);
            g2.drawString(type.toString() + ": " + resourceAmt, x, y);

            y += BAR_HEIGHT + MARGIN;
        }
    }

    private Color getResourceColor(ResourceType type) {
        switch (type) {
            case COPPER:
                return Color.ORANGE;
            case COGS:
                return Color.RED;
            case AETHER:
                return Color.MAGENTA;
            case STEAM:
                return Color.BLUE;
            default:
                return Color.WHITE;
        }
    }

}

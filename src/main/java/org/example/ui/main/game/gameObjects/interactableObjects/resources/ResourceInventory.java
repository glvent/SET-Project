package org.example.ui.main.game.resources;

import org.example.ui.main.GamePanel;
import org.example.ui.utils.Util;

import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class ResourceInventory {
    private final Map<ResourceType, Integer> resources;
    private static final int BASE_RESOURCE_CAP = 1000;
    private GamePanel gp;

    public ResourceInventory(GamePanel gp) {
        resources = new EnumMap<>(ResourceType.class);
        this.gp = gp;

        for (ResourceType type : ResourceType.values()) {
            resources.put(type, 0);
        }

        addResource(ResourceType.COPPER, 250);
        addResource(ResourceType.COGS, 250);
        addResource(ResourceType.STEAM, 500);
    }

    public void addResource(ResourceType type, int amt) {
        if (resources.get(type) + amt <= BASE_RESOURCE_CAP) {
            resources.put(type, resources.get(type) + amt);
        }
    }

    public void consumeResource(ResourceType type, int amt) {
        // does not go below 0
        if (resources.get(type) >= amt) {
            resources.put(type, resources.get(type) - amt);
        }
    }

    public int getResourceAmt(ResourceType type) {
        return resources.get(type);
    }

    public void render(Graphics2D g2) {
        final int BAR_WIDTH = 200;
        final int BAR_HEIGHT = 20;
        final int MARGIN = 20;

        g2.setFont(Util.loadFont("/gameFont.ttf", 16));
        FontMetrics fm = g2.getFontMetrics();

        int x = gp.getWidth() - BAR_WIDTH - MARGIN;
        int y = BAR_HEIGHT;

        for (ResourceType type : ResourceType.values()) {
            int RESOURCE_AMT = getResourceAmt(type);
            // replace 100 with storageBuilding.resourceCap.get(type);
            int FILLED_WIDTH = (int) (((double) RESOURCE_AMT / BASE_RESOURCE_CAP) * BAR_WIDTH);

            g2.setColor(new Color(20, 20, 20, 50));
            g2.fillRoundRect(x, y, BAR_WIDTH, BAR_HEIGHT, 10, 10);

            g2.setColor(getResourceColor(type));
            g2.fillRoundRect(x, y, FILLED_WIDTH, BAR_HEIGHT, 10, 10);

            String text = type.getName() + ": " + RESOURCE_AMT;
            int TEXT_HEIGHT = fm.getAscent() - fm.getDescent();
            int TEXT_Y = y + (BAR_HEIGHT - TEXT_HEIGHT) / 2 + fm.getAscent();

            g2.setColor(Color.WHITE);
            g2.drawString(text, x + 5, TEXT_Y);

            y += BAR_HEIGHT + MARGIN;
        }
        g2.setFont(gp.gameFont24f);
    }

    private Color getResourceColor(ResourceType type) {
        return switch (type) {
            case COPPER -> Color.ORANGE;
            case COGS -> Color.RED;
            case AETHER -> Color.MAGENTA;
            case STEAM -> Color.BLUE;
            default -> Color.WHITE;
        };
    }

}

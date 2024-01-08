package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.util.Map;

public class CopperMine extends Generator {

    public CopperMine(int x, int y, GamePanel gp) {
        super(x, y, gp);

        description = "Delve into the earth to extract valuable copper, a fundamental " +
                "resource for your town's development. The Copper Mine is essential " +
                "for building advanced structures and crafting intricate machinery.";

        LEVEL_CAP = 4;
        resourcePerHour = 240;
        props = BuildingType.COPPER_MINE;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    protected Map<ResourceType, Integer> getUpgradeCost() {
        switch (level) {
            case 1 -> {
                return Map.of(
                        ResourceType.COPPER, 100,
                        ResourceType.COGS, 100,
                        ResourceType.STEAM, 50
                );
            }
            case 2 -> {
                return Map.of(
                        ResourceType.COPPER, 200,
                        ResourceType.COGS, 200,
                        ResourceType.STEAM, 100
                );
            }
            case 3 -> {
                return Map.of(
                        ResourceType.COPPER, 500,
                        ResourceType.COGS, 500,
                        ResourceType.STEAM, 250
                );
            }
        }
        return null;
    }

    @Override
    protected int getUpgradeTime() {
        switch (level) {
            case 1 -> {
                return 60;
            }
            case 2 -> {
                return 150;
            }
            case 3 -> {
                return 500;
            }
        }
        return 0;
    }

    @Override
    protected void changeStatsOnUpgrade() {
        switch (level) {
            case 1 -> {
                resourcePerHour = 400;
            }
            case 2 -> {
                resourcePerHour = 800;
            }
            case 3 -> {
                resourcePerHour = 1200;
            }
        }
    }

    @Override
    protected ResourceType getResourceType() {
        return ResourceType.COPPER;
    }
}

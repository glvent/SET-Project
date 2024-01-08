package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TownHall extends Building {

    public TownHall(int x, int y, GamePanel gp) {
        super(x, y, gp);

        description = "The heart of your settlement, the Town Hall stands " +
                "as a symbol of progress and leadership. It coordinates the " +
                "activities of your town and allows for further expansion " +
                "and advancement. Upgrading the Town Hall unlocks new buildings " +
                "and opportunities.";

        LEVEL_CAP = 4;
        props = BuildingType.TOWN_HALL;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    protected Map<ResourceType, Integer> getUpgradeCost() {
        switch (level) {
            case 1 -> {
                return Map.of(
                        ResourceType.COPPER, 200,
                        ResourceType.COGS, 200,
                        ResourceType.STEAM, 200
                );
            }
            case 2 -> {
                return Map.of(
                        ResourceType.COPPER, 500,
                        ResourceType.COGS, 500,
                        ResourceType.STEAM, 500
                );
            }
            case 3 -> {
                return Map.of(
                        ResourceType.COPPER, 1000,
                        ResourceType.COGS, 1000,
                        ResourceType.STEAM, 1000
                );
            }
        }
        return null;
    }

    @Override
    protected int getUpgradeTime() {
        switch (level) {
            case 1 -> {
                return 200;
            }
            case 2 -> {
                return 500;
            }
            case 3 -> {
                return 1000;
            }
        }
        return 0;
    }

    @Override
    protected void changeStatsOnUpgrade() {

    }

    @Override
    protected void renderBuildingGameAdditions(Graphics2D g2) {

    }

    @Override
    protected void renderBuildingModalAdditions(Graphics2D g2) {

    }

    @Override
    protected void handleBuildingSpecificUpdates() {

    }

    public int getNumOfBuildingsByLevel(BuildingType buildingType) {
        // change to map, so I can take advantage of the changeStatsOnUpgrade abstract method
        switch (buildingType) {
            case BARRACKS -> {
                return 2;
            }
            case COPPER_MINE -> {
                switch (level) {
                    case 1 -> {
                        return 2;
                    }
                    case 2 -> {
                        return 3;
                    }
                    case 3, 4 -> {
                        return 4;
                    }
                    case 5, 6 -> {
                        return 5;
                    }
                    case 7, 8 -> {
                        return 6;
                    }
                }
            }
            case STORAGE_HALL, STEAM_GENERATOR -> {
                switch (level) {
                    case 1, 2 -> {
                        return 1;
                    }
                    case 3, 4 -> {
                        return 2;
                    }
                    case 5, 6 -> {
                        return 3;
                    }
                    case 7, 8 -> {
                        return 4;
                    }
                }
            }
            default -> {
                return 1;
            }
        }
        return 0;
    }

}

package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TownHall extends Building {
    private final Map<ResourceType, Integer> addedResourceCap;

    public TownHall(int x, int y, GamePanel gp) {
        super(x, y, gp);

        discription = "The heart of your settlement, the Town Hall stands " +
                "as a symbol of progress and leadership. It coordinates the " +
                "activities of your town and allows for further expansion " +
                "and advancement. Upgrading the Town Hall unlocks new buildings " +
                "and opportunities.";

        LEVEL_CAP = 8;
        addedResourceCap = new HashMap<>();
        props = BuildingType.TOWN_HALL;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    protected void onUpgrade() {

    }

    @Override
    protected void renderBuildingGameAdditions(Graphics2D g2) {

    }

    @Override
    protected void renderBuildingModalAdditions(Graphics2D g2) {

    }

    public int getNumOfBuildingsByLevel(BuildingType buildingType) {
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

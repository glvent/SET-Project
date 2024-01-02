package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.util.Map;

public class SteamGenerator extends Generator {

    public SteamGenerator(int x, int y, GamePanel gp) {
        super(x, y, gp);

        description = "Harnessing the power of steam, this Generator is " +
                "the backbone of your town's industry. It produces the vital " +
                "energy required to power various buildings and contraptions, " +
                "fueling the growth and innovation of your settlement.";

        resourcePerHour = 120;
        props = BuildingType.STEAM_GENERATOR;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    protected Map<ResourceType, Integer> getUpgradeCost() {
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
                resourcePerHour = 200;
            }
            case 2 -> {
                resourcePerHour = 400;
            }
            case 3 -> {
                resourcePerHour = 600;
            }
        }
    }

    @Override
    protected ResourceType getResourceType() {
        return ResourceType.STEAM;
    }
}

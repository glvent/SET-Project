package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.awt.*;
import java.util.Map;

public class SteamLab extends Building {

    public SteamLab(int x, int y, GamePanel gp) {
        super(x, y, gp);

        description = "A center of innovation and discovery, the Steam Lab is " +
                "where new technologies are researched and knowledge is expanded. " +
                "Upgrade the lab to unlock powerful skills and revolutionary advancements.";

        LEVEL_CAP = 4;
        props = BuildingType.STEAM_LAB;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    protected Map<ResourceType, Integer> getUpgradeCost() {
        switch (level) {
            case 1 -> {
                return Map.of(
                        ResourceType.COPPER, 200,
                        ResourceType.COGS, 200,
                        ResourceType.STEAM, 100
                );
            }
            case 2 -> {
                return Map.of(
                        ResourceType.COPPER, 500,
                        ResourceType.COGS, 500,
                        ResourceType.STEAM, 200
                );
            }
            case 3 -> {
                return Map.of(
                        ResourceType.COPPER, 800,
                        ResourceType.COGS, 800,
                        ResourceType.STEAM, 400
                );
            }
        }
        return null;
    }

    @Override
    protected int getUpgradeTime() {
        switch (level) {
            case 1 -> {
                return 150;
            }
            case 2 -> {
                return 400;
            }
            case 3 -> {
                return 700;
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
}

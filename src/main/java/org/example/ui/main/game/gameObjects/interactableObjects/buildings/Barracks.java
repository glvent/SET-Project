package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.awt.*;
import java.util.Map;

public class Barracks extends Building {
    private int troopCap = 5;
    private int timeToTrain = 30;

    public Barracks(int x, int y, GamePanel gp) {
        super(x, y, gp);

        description =  "A hub for military might, the Barracks trains " +
                "your bravest citizens into skilled warriors. Upgrade to " +
                "bolster your defenses, unlock advanced units, and prepare " +
                "for any threats that dare challenge your domain.";

        LEVEL_CAP = 4;
        props = BuildingType.BARRACKS;
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
                return 20;
            }
            case 2 -> {
                return 60;
            }
            case 3 -> {
                return 200;
            }
        }
        return 0;
    }

    @Override
    protected void changeStatsOnUpgrade() {
        switch (level) {
            case 1 -> {
                troopCap = 6;
                timeToTrain = 25;
            }
            case 2 -> {
                troopCap = 7;
                timeToTrain = 20;
            }
            case 3 -> {
                troopCap = 8;
                timeToTrain = 15;
            }
        }
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

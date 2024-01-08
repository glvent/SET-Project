package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.awt.*;
import java.util.Map;

public class StorageHall extends Building {
    private static final int INIT_ADDED_CAP = 250; // should NOT re-add to cap after reading from DB b/c it is already stored in ResourceInventory

    public StorageHall(int x, int y, GamePanel gp) {
        super(x, y, gp);

        description = "The Storage Hall is crucial for holding your town's resources. " +
                "As you expand and gather more materials, upgrading the Storage Hall " +
                "ensures you have enough space to store your valuable resources securely.";

        LEVEL_CAP = 4;
        props = BuildingType.STORAGE_HALL;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
        changeStatsOnUpgrade();
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
        // we can also resort to unique caps as well but this seems suitable
        int addedCap = level == 1 ? INIT_ADDED_CAP : (int) (INIT_ADDED_CAP * level * 0.75);
        for (ResourceType type : ResourceType.values()) {
            gp.resourceInventory.increaseResourceCap(type, addedCap);
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

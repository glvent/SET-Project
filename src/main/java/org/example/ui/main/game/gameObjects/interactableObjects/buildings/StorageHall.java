package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StorageHall extends Building {
    private Map<ResourceType, Integer> addedResourceCap;

    public StorageHall(int x, int y, GamePanel gp) {
        super(x, y, gp);

        discription = "The Storage Hall is crucial for holding your town's resources. " +
                "As you expand and gather more materials, upgrading the Storage Hall " +
                "ensures you have enough space to store your valuable resources securely.";

        LEVEL_CAP = 6;
        addedResourceCap = new HashMap<>();
        props = BuildingType.STORAGE_HALL;
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

}

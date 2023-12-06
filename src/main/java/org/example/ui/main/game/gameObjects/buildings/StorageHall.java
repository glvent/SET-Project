package org.example.ui.main.game.gameObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.GameObject;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.game.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StorageBuilding extends Building {
    private Map<ResourceType, Integer> resourceCap;

    public StorageBuilding(int x, int y, GamePanel gp) {
        super(x, y, gp);

        this.resourceCap = new HashMap<>();
        this.props = BuildingType.STORAGE_HALL;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    public void update() {

    }

    @Override
    public ObjectType getRandomResource() {
        return null;
    }

    @Override
    public void upgrade() {
        level++;
    }
}

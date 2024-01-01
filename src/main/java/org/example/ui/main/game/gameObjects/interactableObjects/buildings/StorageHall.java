package org.example.ui.main.game.gameObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.game.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.util.HashMap;
import java.util.Map;

public class StorageHall extends Building {
    private Map<ResourceType, Integer> addedResourceCap;

    public StorageHall(int x, int y, GamePanel gp) {
        super(x, y, gp);

        LEVEL_CAP = 6;
        this.addedResourceCap = new HashMap<>();
        this.props = BuildingType.STORAGE_HALL;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    protected void onUpgrade() {

    }

    @Override
    public void update() {

    }

    @Override
    public ObjectType getRandomResource() {
        return null;
    }

}

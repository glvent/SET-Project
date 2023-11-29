package org.example.ui.main.gameObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.gameObjects.resources.ResourceType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StorageBuilding extends Building{
    private Map<ResourceType, Integer> resourceCap;

    public StorageBuilding(BuildingType props, GamePanel gp) {
        super(props, gp);

        this.resourceCap = new HashMap<>();
    }

    @Override
    public void upgrade() {
        level++;
    }
}

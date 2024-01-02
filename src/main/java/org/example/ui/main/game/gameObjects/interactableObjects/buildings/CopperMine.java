package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

public class CopperMine extends Generator {

    public CopperMine(int x, int y, GamePanel gp) {
        super(x, y, gp);

        discription = "Delve into the earth to extract valuable copper, a fundamental " +
                "resource for your town's development. The Copper Mine is essential " +
                "for building advanced structures and crafting intricate machinery.";

        LEVEL_CAP = 4;
        resourcePerHour = 240;
        props = BuildingType.COPPER_MINE;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    protected void onUpgrade() {

    }

    @Override
    protected ResourceType getResourceType() {
        return ResourceType.COPPER;
    }
}

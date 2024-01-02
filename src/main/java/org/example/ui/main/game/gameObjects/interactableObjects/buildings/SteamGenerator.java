package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

public class SteamGenerator extends Generator {

    public SteamGenerator(int x, int y, GamePanel gp) {
        super(x, y, gp);

        discription = "Harnessing the power of steam, this Generator is " +
                "the backbone of your town's industry. It produces the vital " +
                "energy required to power various buildings and contraptions, " +
                "fueling the growth and innovation of your settlement.";

        resourcePerHour = 120;
        props = BuildingType.STEAM_GENERATOR;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }


    @Override
    protected void onUpgrade() {

    }

    @Override
    protected ResourceType getResourceType() {
        return ResourceType.STEAM;
    }
}

package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.map.GameMap;

import java.awt.*;

public class SteamLab extends Building {

    public SteamLab(int x, int y, GamePanel gp) {
        super(x, y, gp);

        discription = "A center of innovation and discovery, the Steam Lab is " +
                "where new technologies are researched and knowledge is expanded. " +
                "Upgrade the lab to unlock powerful skills and revolutionary advancements.";

        LEVEL_CAP = 4;
        props = BuildingType.STEAM_LAB;
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

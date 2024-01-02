package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.map.GameMap;

import java.awt.*;

public class Barracks extends Building {
    private int troopCap = 5;
    private int timeToTrain = 30; // in seconds

    public Barracks(int x, int y, GamePanel gp) {
        super(x, y, gp);

        discription =  "A hub for military might, the Barracks trains " +
                "your bravest citizens into skilled warriors. Upgrade to " +
                "bolster your defenses, unlock advanced units, and prepare " +
                "for any threats that dare challenge your domain.";

        LEVEL_CAP = 4;
        props = BuildingType.BARRACKS;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    protected void onUpgrade() {
         switch (level) {
             case 2, 3, 4 -> {
                 troopCap++;
             }
         }
    }

    @Override
    protected void renderBuildingGameAdditions(Graphics2D g2) {

    }

    @Override
    protected void renderBuildingModalAdditions(Graphics2D g2) {

    }
}

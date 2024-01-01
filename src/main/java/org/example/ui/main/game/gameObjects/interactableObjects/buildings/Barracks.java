package org.example.ui.main.game.gameObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.map.GameMap;

public class Barracks extends Building {
    private int troopCap = 5;

    public Barracks(int x, int y, GamePanel gp) {
        super(x, y, gp);

        LEVEL_CAP = 4;
        this.props = BuildingType.BARRACKS;
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

    }

    @Override
    protected void onUpgrade() {
         switch (level) {
             case 2 -> {

             }
         }
    }
}

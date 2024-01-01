package org.example.ui.main.game.gameObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.map.GameMap;

public class SteamLab extends Building {

    public SteamLab(int x, int y, GamePanel gp) {
        super(x, y, gp);

        this.props = BuildingType.STEAM_LAB;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    @Override
    public void update() {
        // entity health / damage dealt... with upgrades... like coc

        switch (level) {

        }
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

    }
}

package org.example.ui.main.game.gameObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;

public class ResearchHall extends Building {

    public ResearchHall(int x, int y, GamePanel gp) {
        super(x, y, gp);
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
}

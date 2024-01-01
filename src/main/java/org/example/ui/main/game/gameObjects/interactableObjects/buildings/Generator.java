package org.example.ui.main.game.gameObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;

public class Generator extends Building {
    protected int resourcePerHour;
    protected long lastUpdateTime;

    public Generator(int x, int y, GamePanel gp) {
        super(x, y, gp);

        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void update() {

    }

    @Override
    public ObjectType getRandomResource() {
        return null;
    }

    @Override
    protected void onUpgrade() {

    }
}

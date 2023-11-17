package org.example.ui.gameObjects;

import org.example.ui.input.KeyHandler;
import org.example.ui.main.GamePanel;
import org.example.ui.utils.Position;
import org.example.ui.utils.Vector;


public class Player extends Entity {
    private static final Position DEFAULT_POSITION = new Position(GamePanel.SCREEN_WIDTH / 2, GamePanel.SCREEN_WIDTH / 2);
    private static final Vector DEFAULT_VECTOR = new Vector(4.0, 4.0);
    KeyHandler keyH;


    public Player(KeyHandler keyH) {
        super(DEFAULT_POSITION, DEFAULT_VECTOR, false, true, null);
        this.keyH = keyH;
    }

    public void update() {
        switch (keyH.keyPressed) {
            case 'W' -> position.y -= vector.veloY;
            case 'A' -> position.x -= vector.veloX;
            case 'S' -> position.y += vector.veloY;
            case 'D' -> position.x += vector.veloX;
        }
    }
}

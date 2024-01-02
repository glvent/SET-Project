package org.example.ui.main.game.gameObjects.staticObjects;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.GameObject;
import org.example.ui.main.game.gameObjects.ObjectType;

import java.awt.*;

public class Cloud extends GameObject {
    ObjectType props;

    public Cloud(int x, int y, GamePanel gp) {
        super(x, y, gp);
        props = getRandomResource();
        bounds.setSize(props.getImage().getWidth(null), props.getImage().getHeight(null));
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(props.getImage(), bounds.x, bounds.y, null);
    }

    public ObjectType getRandomResource() {
        ObjectType[] clouds = new ObjectType[]{
                ObjectType.CLOUD1,
                ObjectType.CLOUD2,
                ObjectType.CLOUD3,
                ObjectType.CLOUD4,
                ObjectType.CLOUD5,
                ObjectType.CLOUD6,
                ObjectType.CLOUD7,
        };

        return clouds[(int) (Math.random() * clouds.length)];
    }

}

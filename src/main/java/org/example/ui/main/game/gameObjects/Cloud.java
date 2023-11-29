package org.example.ui.main.gameObjects.objects;

import org.example.ui.main.GamePanel;

import java.awt.*;

public class Cloud extends GameObject {
    public Cloud(int x, int y, GamePanel gp) {
        super(x, y, gp);
        props = getRandomCloud();

        this.bounds = new Rectangle(x, y, props.getImage().getWidth(null),
                props.getImage().getHeight(null));
    }

    public ObjectType getRandomCloud() {
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

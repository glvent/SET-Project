package org.example.ui.main.game.gameObjects.staticObjects;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.GameObject;
import org.example.ui.main.game.gameObjects.ObjectType;

import java.awt.*;

public class Decoration extends GameObject {
    protected ObjectType props;

    public Decoration(int x, int y, GamePanel gp) {
        super(x, y, gp);
        props = getRandomResource();
        bounds.setSize(props.getImage().getWidth(null), props.getImage().getHeight(null));
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(props.getImage(), bounds.x, bounds.y, null);

    }

    public ObjectType getRandomResource() {
        ObjectType[] decorations = new ObjectType[]{
                ObjectType.MUSHROOM,
                ObjectType.ROCK,
                ObjectType.TREE,
                ObjectType.LOG
        };
        return decorations[(int) (Math.random() * decorations.length)];
    }
}

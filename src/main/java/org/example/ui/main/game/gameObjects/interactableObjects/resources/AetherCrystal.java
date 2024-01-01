package org.example.ui.main.game.gameObjects;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.GameObject;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.game.gameObjects.ResourceNode;
import org.example.ui.main.game.gameObjects.buildings.BuildingType;

import java.awt.*;

public class AetherCrystal extends ResourceNode {
    public AetherCrystal(int x, int y, GamePanel gp) {
        super(x, y, gp);
        props = getRandomResource();
        bounds.setSize(props.getImage().getWidth(null), props.getImage().getHeight(null));
    }

    @Override
    public void update() {

    }

    @Override
    public ObjectType getRandomResource() {
        ObjectType[] aetherCrystals = new ObjectType[]{
                ObjectType.AETHER_CRYSTAL1,
                ObjectType.AETHER_CRYSTAL2,
                ObjectType.AETHER_CRYSTAL3,
                ObjectType.AETHER_CRYSTAL4
        };

        return aetherCrystals[(int) (Math.random() * aetherCrystals.length)];
    }
}

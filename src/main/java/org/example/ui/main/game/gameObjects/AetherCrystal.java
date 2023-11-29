package org.example.ui.main.game;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.GameObject;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.game.gameObjects.ResourceNode;

import java.awt.*;

public class AetherCrystal extends ResourceNode {
    public AetherCrystal(int x, int y, GamePanel gp) {
        super(x, y, gp);
        props = getRandomAetherCrystal();

        this.bounds = new Rectangle(x, y, props.getImage().getWidth(null),
                props.getImage().getHeight(null));
    }

    public ObjectType getRandomAetherCrystal() {
        ObjectType[] aetherCrystals = new ObjectType[]{
                ObjectType.AETHER_CRYSTAL1,
                ObjectType.AETHER_CRYSTAL2,
                ObjectType.AETHER_CRYSTAL3,
                ObjectType.AETHER_CRYSTAL4
        };

        return aetherCrystals[(int) (Math.random() * aetherCrystals.length)];
    }

}

package org.example.ui.main.game.gameObjects.interactableObjects.resources;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.ObjectType;
import org.example.ui.main.game.gameObjects.ResourceNode;

public class CopperVein extends ResourceNode {
    public CopperVein(int x, int y, GamePanel gp) {
        super(x, y, gp);
        props = getRandomResource();
        bounds.setSize(props.getImage().getWidth(null), props.getImage().getHeight(null));
    }

    public ObjectType getRandomResource() {
        ObjectType[] copperVeins = new ObjectType[]{
                ObjectType.COPPER_VEIN1,
                ObjectType.COPPER_VEIN2,
                ObjectType.COPPER_VEIN3,
                ObjectType.COPPER_VEIN4
        };

        return copperVeins[(int) (Math.random() * copperVeins.length)];
    }
}

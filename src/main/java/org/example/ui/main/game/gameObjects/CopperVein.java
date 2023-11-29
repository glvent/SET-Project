package org.example.ui.main.gameObjects.objects;

import org.example.ui.main.GamePanel;

import java.awt.*;

public class CopperVein extends GameObject {
    public CopperVein(int x, int y, GamePanel gp) {
        super(x, y, gp);
        props = getRandomCopperVein();

        this.bounds = new Rectangle(x, y, props.getImage().getWidth(null),
                props.getImage().getHeight(null));
    }

    public ObjectType getRandomCopperVein() {
        ObjectType[] copperVeins = new ObjectType[]{
                ObjectType.COPPER_VEIN1,
                ObjectType.COPPER_VEIN2,
                ObjectType.COPPER_VEIN3,
                ObjectType.COPPER_VEIN4
        };

        return copperVeins[(int) (Math.random() * copperVeins.length)];
    }

}

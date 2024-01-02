package org.example.ui.main.game.gameObjects;

import org.example.ui.main.GamePanel;

import java.awt.*;

public abstract class ResourceNode extends GameObject {
    protected ObjectType props;
    public static int AMT_GIVEN;

    public ResourceNode(int x, int y, GamePanel gp) {
        super(x, y, gp);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(props.getImage(), bounds.x, bounds.y, null);
    }

}

package org.example.ui.main.gameObjects.buildings;

import org.example.ui.main.GamePanel;

import java.awt.*;

public abstract class Building {
    protected int level;
    protected BuildingType props;
    protected GamePanel gp;

    public Building(BuildingType props, GamePanel gp) {
        this.props = props;
        this.level = 1;
        this.gp = gp;
    }

    public void render(Graphics2D g2) {

    }

    public abstract void upgrade();

    public int getLevel() {
        return level;
    }

    public BuildingType getProps() {
        return props;
    }
}

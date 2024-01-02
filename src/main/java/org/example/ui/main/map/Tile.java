package org.example.ui.main.map;

import java.awt.*;

public class Tile {
    // eventually change to public
    private TileType props;
    private Rectangle bounds;
    private boolean isExplored;
    private Fog fog;


    public Tile(TileType props, int x, int y, int width, int height) {
        this.props = props;
        this.bounds = new Rectangle(x, y, width, height);

        isExplored = false;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public TileType getProps() {
        return props;
    }

    public Fog getFog() {
        return fog;
    }

    public boolean isExplored() {
        return isExplored;
    }

    public void setProps(TileType props) {
        this.props = props;
    }

    public void setFog(Fog fog) {
        this.fog = fog;
    }

    public void setExplored(boolean explored) {
        isExplored = explored;
    }
}

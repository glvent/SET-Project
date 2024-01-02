package org.example.ui.main.map;

import java.awt.*;

import static org.example.ui.utils.Util.readImage;

public enum TileType {
    AIR("Air", false, false, readImage("/air.png")),
    GRASS("Island", false, true, readImage("/grass.png")),
    GRASS_TOP_LEFT("Island", false, true, readImage("/grassTopLeft.png")),
    GRASS_TOP_LEFT_CORNER("Island", false, true, readImage("/grassTopLeftCorner.png")),
    GRASS_TOP("Island", false, true, readImage("/grassTop.png")),
    GRASS_TOP_RIGHT("Island", false, true, readImage("/grassTopRight.png")),
    GRASS_TOP_RIGHT_CORNER("Island", false, true, readImage("/grassTopRightCorner.png")),
    GRASS_RIGHT("Island", false, true, readImage("/grassRight.png")),
    GRASS_BOTTOM_RIGHT("Island", false, true, readImage("/grassBottomRight.png")),
    GRASS_BOTTOM_RIGHT_CORNER("Island", false, true, readImage("/grassBottomRightCorner.png")),
    GRASS_BOTTOM("Island", false, true, readImage("/grassBottom.png")),
    GRASS_BOTTOM_LEFT("Island", false, true, readImage("/grassBottomLeft.png")),
    GRASS_BOTTOM_LEFT_CORNER("Island", false, true, readImage("/grassBottomLeftCorner.png")),
    GRASS_LEFT("Island", false, true, readImage("/grassLeft.png"));

    private final String name;
    private final boolean isCollisionable;
    private final boolean isBuildable;
    private final Image image;

    TileType(String name, boolean isCollisionable, boolean isBuildable, Image image) {
        this.name = name;
        this.isCollisionable = isCollisionable;
        this.isBuildable = isBuildable;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public boolean isCollisionable() {
        return isCollisionable;
    }

    public boolean isBuildable() {
        return isBuildable;
    }

    public Image getImage() {
        return image;
    }
}

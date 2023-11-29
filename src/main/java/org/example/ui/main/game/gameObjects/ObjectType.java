package org.example.ui.main.gameObjects.objects;

import org.example.ui.main.map.TileType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static org.example.ui.utils.Util.readImage;

public enum ObjectType {
    CLOUD1("Cloud 1", false, false, readImage("/cloud1.png")),
    CLOUD2("Cloud 2", false, false, readImage("/cloud2.png")),
    CLOUD3("Cloud 3", false, false, readImage("/cloud3.png")),
    CLOUD4("Cloud 4", false, false, readImage("/cloud4.png")),
    CLOUD5("Cloud 5", false, false, readImage("/cloud5.png")),
    CLOUD6("Cloud 6", false, false, readImage("/cloud6.png")),
    CLOUD7("Cloud 7", false, false, readImage("/cloud7.png")),
    COPPER_VEIN1("Copper Vein 1", true, true, readImage("/copperVein1.png")),
    COPPER_VEIN2("Copper Vein 2", true, true, readImage("/copperVein2.png")),
    COPPER_VEIN3("Copper Vein 3", true, true, readImage("/copperVein3.png")),
    COPPER_VEIN4("Copper Vein 4", true, true, readImage("/copperVein4.png")),
    AETHER_CRYSTAL1("Aether Crystal 1", true, true, readImage("/aetherCrystal1.png")),
    AETHER_CRYSTAL2("Aether Crystal 2", true, true, readImage("/aetherCrystal2.png")),
    AETHER_CRYSTAL3("Aether Crystal 3", true, true, readImage("/aetherCrystal3.png")),
    AETHER_CRYSTAL4("Aether Crystal 4", true, true, readImage("/aetherCrystal4.png"));


    private final String name;
    private final boolean isCollisionable;
    private final boolean isMineable;
    private final Image image;

    ObjectType(String name, boolean isCollisionable, boolean isMineable, Image image) {
        this.name = name;
        this.isCollisionable = isCollisionable;
        this.isMineable = isMineable;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public boolean isCollisionable() {
        return isCollisionable;
    }

    public boolean isMineable() {
        return isMineable;
    }

    public Image getImage() {
        return image;
    }

}

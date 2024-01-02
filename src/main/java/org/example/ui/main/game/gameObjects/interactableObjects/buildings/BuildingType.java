package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.example.ui.utils.Util.readImage;

public enum BuildingType {
    BARRACKS("Barracks", readImage("/"), new Dimension(2, 2),
            Map.of(ResourceType.COPPER, 50, ResourceType.COGS, 50, ResourceType.STEAM, 10)),
    STEAM_GENERATOR("Steam Generator", readImage("/"), new Dimension(2, 2),
            Map.of(ResourceType.COPPER, 50, ResourceType.COGS, 50, ResourceType.STEAM, 10)),
    STEAM_LAB("Steam Lab", readImage("/"), new Dimension(3, 3),
            Map.of(ResourceType.COPPER, 75, ResourceType.COGS, 50, ResourceType.STEAM, 25)),
    COPPER_MINE("Copper Mine", readImage("/"), new Dimension(2, 2),
            Map.of(ResourceType.COPPER, 50, ResourceType.COGS, 50, ResourceType.STEAM, 10)),
    STORAGE_HALL("Storage Hall", readImage("/"), new Dimension(3, 2),
            Map.of(ResourceType.COPPER, 75, ResourceType.COGS, 50, ResourceType.STEAM, 25)),
    TOWN_HALL("Town Hall", readImage("/"), new Dimension(3, 3), null);

    // eventually dimensions will be replaced with the image dimensions

    private final String name;
    private final Image image;
    private final Dimension dimensions;
    private final Map<ResourceType, Integer> cost;

    BuildingType(String name, Image image, Dimension dimensions, Map<ResourceType, Integer> cost) {
        this.name = name;
        this.image = image;
        this.dimensions = dimensions;
        // cannot change init cost
        this.cost = cost != null ? Collections.unmodifiableMap(new HashMap<>(cost)) : null;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public Dimension getDimensions() {
        return dimensions;
    }

    public Map<ResourceType, Integer> getCost() {
        return cost;
    }

}

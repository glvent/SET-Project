package org.example.ui.main;

public enum TileType {
    AIR,
    ISLAND,
    BUILDING_SPOT,
    BUILDING,
    RESOURCE_NODE,
    PATH,
    WATER,
    OBSTACLE,

    private final String name;
    private final boolean isCollisionable;
    private final boolean isBuildable;

    TileType(String name, boolean isCollisionable, boolean isBuildable) {
        this.name = name;
        this.isCollisionable = isCollisionable;
        this.isBuildable = isBuildable;
    }

    
}

package org.example.ui.main.gameObjects.resources;

public enum ResourceType {
    AETHER("Aether"),
    COGS("Cogs"),
    RAW_COPPER("Raw Copper"),
    COPPER("Copper"),
    STEAM("Steam");

    private final String name;

    ResourceType(String name) {
        this.name = name;
    }
}

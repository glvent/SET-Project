package org.example.ui.main.game.gameObjects.interactableObjects.resources;

public enum ResourceType {
    AETHER("Aether"),
    COGS("Cogs"),
    COPPER("Copper"),
    STEAM("Steam");

    private final String name;

    ResourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

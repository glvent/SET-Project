package org.example.ui.main.game.gameInterface;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.GameObject;
import org.example.ui.main.game.gameObjects.interactableObjects.buildings.*;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;

public class Builder {
    private GamePanel gp;
    private final Rectangle bounds;
    private boolean isModalOpen = false;
    private boolean inBuildMode = false;
    private BuildingType selectedBuilding;
    private int scrollOffset = 0;
    private Rectangle scrollbarHandle;
    private boolean canPlaceBuilding = true;

    // make a new GUI class and instantiate/render all buttons & or dialogue in there?

    public Builder(GamePanel gp) {
        this.gp = gp;

        final int BUTTON_SIZE = 75;
        final int X_MARGIN = 12;
        final int Y_MARGIN = 40;
        final int x = GamePanel.SCREEN_WIDTH - BUTTON_SIZE - X_MARGIN;
        final int y = GamePanel.SCREEN_HEIGHT - BUTTON_SIZE - Y_MARGIN;

        bounds = new Rectangle(x, y, BUTTON_SIZE, BUTTON_SIZE);
    }

    public void render(Graphics2D g2) {
        Color buttonColor = new Color(20, 20, 20, 50);
        if (gp.mouseH.guiRelativeMousePosition != null && bounds.contains(gp.mouseH.guiRelativeMousePosition)) {
            g2.setColor(buttonColor.brighter().brighter());
        } else {
            g2.setColor(buttonColor);
        }

        g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);

        String buttonText = "Shop";
        FontMetrics fm = g2.getFontMetrics();
        int textX = bounds.x + (bounds.width - fm.stringWidth(buttonText)) / 2;
        int textY = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();

        g2.setColor(Color.WHITE);
        g2.drawString(buttonText, textX, textY);

        if (gp.mouseH.guiRelativeClick != null && bounds.contains(gp.mouseH.guiRelativeClick)) {
            isModalOpen = !isModalOpen;
            deselectBuilding();
            gp.mouseH.guiRelativeClick.setLocation(-1, -1);
        }

        if (isModalOpen) {
            renderModal(g2);
        }

        if (inBuildMode) {
            renderBuildGrid(g2);
        }

        if (gp.keyH.getCurrentKeyEvent() == KeyEvent.VK_ESCAPE) {
            deselectBuilding();
        }

        build();
    }

    private void build() {
        if (selectedBuilding != null && gp.keyH.getCurrentKeyEvent() == KeyEvent.VK_ENTER && canPlaceBuilding) {
            int x = (getTilePositionFromMouse().x - 1) * GameMap.TILE_SIZE;
            int y = (getTilePositionFromMouse().y - 1) * GameMap.TILE_SIZE;

            Map<ResourceType, Integer> buildingCost = selectedBuilding.getCost();

            if (canAffordBuilding(buildingCost) && isValidBuildingLocation(x, y, selectedBuilding)) {
                Building buildingToAdd = createBuilding(x, y, selectedBuilding);
                deductResources(buildingCost);
                gp.gameMap.addGameObject(buildingToAdd);
            }

            deselectBuilding();
        }
    }

    private boolean isValidBuildingLocation(int x, int y, BuildingType buildingType) {
        return gp.gameMap.isTileBuildable(x / GameMap.TILE_SIZE, y / GameMap.TILE_SIZE) && !gp.gameMap.isTileOccupied(x / GameMap.TILE_SIZE, y / GameMap.TILE_SIZE, buildingType.getDimensions());
    }

    private boolean canAffordBuilding(Map<ResourceType, Integer> buildingCost) {
        for (Map.Entry<ResourceType, Integer> cost : buildingCost.entrySet()) {
            if (cost.getValue() > gp.resourceInventory.getResourceAmt(cost.getKey())) {
                return false;
            }
        }
        return true;
    }

    private void deductResources(Map<ResourceType, Integer> buildingCost) {
        for (Map.Entry<ResourceType, Integer> cost : buildingCost.entrySet()) {
            gp.resourceInventory.consumeResource(cost.getKey(), cost.getValue());
        }
    }

    private Building createBuilding(int x, int y, BuildingType type) {
        Building buildingToAdd = null;

        switch (type) {
            case TOWN_HALL -> buildingToAdd = new TownHall(x, y, gp);
            case BARRACKS -> buildingToAdd = new Barracks(x, y, gp);
            case STEAM_GENERATOR -> buildingToAdd = new SteamGenerator(x, y, gp);
            case STEAM_LAB -> buildingToAdd = new SteamLab(x, y, gp);
            case COPPER_MINE -> buildingToAdd = new CopperMine(x, y, gp);
            case STORAGE_HALL -> buildingToAdd = new StorageHall(x, y, gp);
        }
        return buildingToAdd;
    }

    // move to building class?
    private Class<? extends Building> propsToClass(BuildingType props) {
        switch (props) {
            case TOWN_HALL -> {
                return TownHall.class;
            }
            case COPPER_MINE -> {
                return CopperMine.class;
            }
            case BARRACKS -> {
                return Barracks.class;
            }
            case STEAM_GENERATOR -> {
                return SteamGenerator.class;
            }
            case STORAGE_HALL -> {
                return StorageHall.class;
            }
            case STEAM_LAB -> {
                return SteamLab.class;
            }
            default -> {
                return null;
            }
        }
    }

    private void renderModal(Graphics2D g2) {
        final int SCROLLBAR_HEIGHT = 15;
        final int MODAL_WIDTH = 600;
        final int MODAL_HEIGHT = 250;
        final int MODAL_ITEM_WIDTH = 200;
        final int TOTAL_MODAL_WIDTH = (BuildingType.values().length - 1) * MODAL_ITEM_WIDTH;
        final boolean NEEDS_SCROLL = TOTAL_MODAL_WIDTH > MODAL_WIDTH;

        int MODAL_X = (gp.SCREEN_WIDTH - MODAL_WIDTH) / 2;
        int MODAL_Y = (gp.SCREEN_HEIGHT - MODAL_HEIGHT) / 2;

        int SCROLL_BAR_Y = MODAL_Y + MODAL_HEIGHT;
        int SCROLLBAR_WIDTH = MODAL_WIDTH;
        g2.setColor(Color.GRAY);
        g2.fillRoundRect(MODAL_X, SCROLL_BAR_Y, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT, 20, 20);

        int HANDLE_WIDTH = (int) ((float) MODAL_WIDTH / TOTAL_MODAL_WIDTH * SCROLLBAR_WIDTH);
        int HANDLE_X = MODAL_X + (int) ((float) scrollOffset / TOTAL_MODAL_WIDTH * SCROLLBAR_WIDTH);
        scrollbarHandle = new Rectangle(HANDLE_X, SCROLL_BAR_Y, HANDLE_WIDTH, SCROLLBAR_HEIGHT);

        g2.setColor(Color.DARK_GRAY);
        g2.fillRoundRect(scrollbarHandle.x, scrollbarHandle.y, scrollbarHandle.width, scrollbarHandle.height, 20, 20);

        g2.setColor(new Color(50, 50, 50, 180));
        g2.fillRoundRect(MODAL_X, MODAL_Y, MODAL_WIDTH, MODAL_HEIGHT, 10, 10);

        g2.setClip(MODAL_X, MODAL_Y, MODAL_WIDTH, MODAL_HEIGHT);

        TownHall townHall = gp.gameMap.getTownHall();

        for (BuildingType building : BuildingType.values()) {
            if (building != BuildingType.TOWN_HALL) {
                // refactor to make either class or enum the norm but not both?
                int currentBuildingCount = gp.gameMap.getTypeOfGameObjects(propsToClass(building)).size();
                int maxBuildingCount = townHall.getNumOfBuildingsByLevel(building);

                int itemX = MODAL_X + building.ordinal() * MODAL_ITEM_WIDTH - scrollOffset;
                Rectangle itemBounds = new Rectangle(itemX, MODAL_Y, MODAL_ITEM_WIDTH, MODAL_HEIGHT);

                // draw name
                g2.setColor(Color.WHITE);
                String buildingName = building.getName();
                g2.drawString(buildingName, itemX + 10, MODAL_Y + 20);
                g2.drawRoundRect(itemBounds.x, itemBounds.y, itemBounds.width, itemBounds.height, 10, 10);

                // draw count
                if (currentBuildingCount == maxBuildingCount) g2.setColor(Color.RED);
                String countText = currentBuildingCount + "/" + maxBuildingCount;
                int countTextX = itemBounds.x + itemBounds.width - 35;
                int countTextY = itemBounds.y + itemBounds.height - 10;
                g2.drawString(countText, countTextX, countTextY);

                // draw costs
                int costTextY = itemBounds.y + itemBounds.height - 10;
                int costTextX = itemBounds.x + 10;
                FontMetrics fm = g2.getFontMetrics();
                int lineHeight = fm.getHeight();
                g2.setFont(gp.gameFont16f);

                for (Map.Entry<ResourceType, Integer> cost : building.getCost().entrySet()) {
                    g2.drawString(cost.getKey() + ": " + cost.getValue(), costTextX, costTextY);
                    costTextY -= lineHeight;
                }

                if (gp.mouseH.guiRelativeClick != null && itemBounds.contains(gp.mouseH.guiRelativeClick)) {
                    selectBuilding(building);
                    gp.mouseH.guiRelativeClick.setLocation(-1, -1);
                }
            }
            g2.setFont(gp.gameFont24f);
        }

        g2.setClip(null);

        if (NEEDS_SCROLL) {
            scrollOffset += gp.mouseH.wheelRotation * 35;
            scrollOffset = Math.max(0, Math.min(scrollOffset, TOTAL_MODAL_WIDTH - MODAL_WIDTH));
            gp.mouseH.wheelRotation = 0;
        }

    }

    private void selectBuilding(BuildingType buildingType) {
        inBuildMode = true;
        selectedBuilding = buildingType;
        isModalOpen = false;
    }

    private void deselectBuilding() {
        inBuildMode = false;
        selectedBuilding = null;
        scrollOffset = 0;
    }

    public void renderBuildGrid(Graphics2D g2) {
        if (gp.mouseH.guiRelativeMousePosition == null || selectedBuilding == null) return;

        int buildingWidth = selectedBuilding.getDimensions().width;
        int buildingHeight = selectedBuilding.getDimensions().height;

        int centerTileX = getTilePositionFromMouse().x;
        int centerTileY = getTilePositionFromMouse().y;

        int buildingStartX = centerTileX - buildingWidth / 2;
        int buildingStartY = centerTileY - buildingHeight / 2;

        int gridCenterX = (buildingStartX + buildingWidth / 2) * GameMap.TILE_SIZE - gp.camera.x;
        int gridCenterY = (buildingStartY + buildingHeight / 2) * GameMap.TILE_SIZE - gp.camera.y;

        canPlaceBuilding = true;

        String buildingName = selectedBuilding.getName();
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(buildingName);
        int textX = gridCenterX - textWidth / 2;
        int textY = gridCenterY - (buildingHeight * GameMap.TILE_SIZE) + 25;

        g2.setFont(gp.gameFont16f);
        g2.drawString(buildingName, textX, textY);
        g2.setFont(gp.gameFont24f);
        for (int bx = 0; bx < buildingWidth; bx++) {
            for (int by = 0; by < buildingHeight; by++) {
                int tileX = buildingStartX + bx;
                int tileY = buildingStartY + by;

                boolean currentTileBuildable = true;

                if (!isWithinMapBounds(tileX, tileY) || !gp.gameMap.getTile(tileX, tileY).getProps().isBuildable()) {
                    currentTileBuildable = false;
                    canPlaceBuilding = false;
                }

                Rectangle buildArea = new Rectangle(tileX * GameMap.TILE_SIZE, tileY * GameMap.TILE_SIZE, GameMap.TILE_SIZE, GameMap.TILE_SIZE);
                for (GameObject gameObject : gp.gameMap.getGameObjects()) {
                    if (buildArea.intersects(gameObject.getBounds())) {
                        currentTileBuildable = false;
                        canPlaceBuilding = false;
                    }
                }

                int rectX = (buildingStartX + bx) * GameMap.TILE_SIZE - gp.camera.x;
                int rectY = (buildingStartY + by) * GameMap.TILE_SIZE - gp.camera.y;

                g2.setColor(currentTileBuildable ? new Color(0, 255, 0, 50) : new Color(255, 0, 0, 50));
                g2.fillRect(rectX, rectY, gp.gameMap.TILE_SIZE, gp.gameMap.TILE_SIZE);
                g2.drawRect(rectX, rectY, gp.gameMap.TILE_SIZE, gp.gameMap.TILE_SIZE);
            }
        }

        g2.setColor(Color.WHITE);
        if (canPlaceBuilding) {
            g2.drawString("ENTER", gp.mouseH.guiRelativeMousePosition.x - 50, gp.mouseH.guiRelativeMousePosition.y + 25);
        }
        g2.drawString("ESC", gp.mouseH.guiRelativeMousePosition.x + 15, gp.mouseH.guiRelativeMousePosition.y + 25);
    }


    private boolean isWithinMapBounds(int x, int y) {
        return x >= 0 && x < gp.gameMap.MAP_WIDTH && y >= 0 && y < gp.gameMap.MAP_HEIGHT;
    }

    private Point getTilePositionFromMouse() {
        int mouseX = gp.mouseH.guiRelativeMousePosition.x + gp.camera.x;
        int mouseY = gp.mouseH.guiRelativeMousePosition.y + gp.camera.y;

        int tileX = mouseX / gp.gameMap.TILE_SIZE;
        int tileY = mouseY / gp.gameMap.TILE_SIZE;

        return new Point(tileX, tileY);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}

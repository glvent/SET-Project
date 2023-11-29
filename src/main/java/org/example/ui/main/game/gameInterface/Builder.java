package org.example.ui.main.gameObjects.gameInterface;

import org.example.ui.main.GamePanel;
import org.example.ui.main.gameObjects.buildings.BuildingType;
import org.example.ui.main.map.Tile;
import org.example.ui.main.map.TileType;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Builder {
    private GamePanel gp;
    private final Rectangle bounds;
    private boolean isOpen = false;
    private boolean inBuildMode = false;
    private BuildingType selectedBuilding;

    // make a new GUI class and instantiate/render all buttons & or dialogue in there

    public Builder(GamePanel gp) {
        this.gp = gp;

        final int BUTTON_SIZE = 75;
        final int X_MARGIN = 12;
        final int Y_MARGIN = 40;
        final int x = GamePanel.SCREEN_WIDTH - BUTTON_SIZE - X_MARGIN;
        final int y = GamePanel.SCREEN_HEIGHT - BUTTON_SIZE - Y_MARGIN;

        this.bounds = new Rectangle(x, y, BUTTON_SIZE, BUTTON_SIZE);
    }

    public void render(Graphics2D g2) {
        if (gp.mouseH.currentMousePosition != null && bounds.contains(gp.mouseH.currentMousePosition)) {
            g2.setColor(new Color(40, 40, 40, 50));
        } else {
            g2.setColor(new Color(20, 20, 20, 50));
        }

        g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, 10, 10);

        String buttonText = "Shop";
        FontMetrics fm = g2.getFontMetrics();
        int textX = bounds.x + (bounds.width - fm.stringWidth(buttonText)) / 2;
        int textY = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();

        g2.setColor(Color.WHITE);
        g2.drawString(buttonText, textX, textY);

        if (gp.mouseH.guiRelativeClick != null && bounds.contains(gp.mouseH.guiRelativeClick)) {
            isOpen = !isOpen;
            gp.mouseH.guiRelativeClick.setLocation(-1, -1);
        }

        if (isOpen) {
            renderMenu(g2);
        }

        if (inBuildMode) {
            renderBuildGrid(g2);
        }

        if (gp.keyH.getCurrentKeyEvent() == KeyEvent.VK_ESCAPE) {
            deselectBuilding();
        }
    }

    private void renderMenu(Graphics2D g2) {
        final int MENU_WIDTH = 200;
        final int MENU_HEIGHT = 300;
        final int MENU_ITEM_HEIGHT = 50;
        int menuX = bounds.x - MENU_WIDTH;
        int menuY = bounds.y - MENU_HEIGHT + bounds.height;

        g2.setColor(new Color(50, 50, 50, 180));
        g2.fillRoundRect(menuX, menuY, MENU_WIDTH, MENU_HEIGHT, 10, 10);

        for (BuildingType building : BuildingType.values()) {
            int itemY = menuY + building.ordinal() * MENU_ITEM_HEIGHT;
            Rectangle itemBounds = new Rectangle(menuX, itemY, MENU_WIDTH, MENU_ITEM_HEIGHT);
            g2.setColor(Color.WHITE);
            g2.drawString(building.name(), menuX + 10, itemY + 20);
            g2.drawRoundRect(itemBounds.x, itemBounds.y, itemBounds.width, itemBounds.height, 10, 10);

                if (gp.mouseH.guiRelativeClick != null && itemBounds.contains(gp.mouseH.guiRelativeClick)) {
                    selectBuilding(building);
                    gp.mouseH.guiRelativeClick.setLocation(-1, -1);
                }
        }
    }

    private void selectBuilding(BuildingType buildingType) {
        inBuildMode = true;
        selectedBuilding = buildingType;
    }

    private void deselectBuilding() {
        inBuildMode = false;
        selectedBuilding = null;
    }

    public void renderBuildGrid(Graphics2D g2) {
        if (gp.mouseH.currentMousePosition == null && selectedBuilding != null) return;

        final int RADIUS = 3;
        final float MAX_ALPHA = 0.25f;

        int mouseX = gp.mouseH.currentMousePosition.x + gp.camera.x;
        int mouseY = gp.mouseH.currentMousePosition.y + gp.camera.y;

        int centerTileX = mouseX / gp.gameMap.getTileWidth();
        int centerTileY = mouseY / gp.gameMap.getTileHeight();

        // top-left of building blueprint
        int buildingStartX = centerTileX - selectedBuilding.getWidth() / 2;
        int buildingStartY = centerTileY - selectedBuilding.getHeight() / 2;

        for (int x = centerTileX - RADIUS; x <= centerTileX + RADIUS; x++) {
            for (int y = centerTileY - RADIUS; y <= centerTileY + RADIUS; y++) {
                double distance = Math.sqrt(Math.pow(x - centerTileX, 2) + Math.pow(y - centerTileY, 2));
                if (distance <= RADIUS && gp.gameMap.getTile(x, y).getProps().isBuildable()) {
                    int rectX = x * gp.gameMap.getTileWidth() - gp.camera.x;
                    int rectY = y * gp.gameMap.getTileHeight() - gp.camera.y;

                    // decreases transparency over distance
                    float alpha = MAX_ALPHA * (1.0f - (float) distance / RADIUS);
                    g2.setColor(new Color(255, 255, 255, (int) (alpha * 255)));

                    g2.drawRect(rectX, rectY, gp.gameMap.getTileWidth(), gp.gameMap.getTileHeight());
                }
            }
        }

        for (int bx = 0; bx < selectedBuilding.getWidth(); bx++) {
            for (int by = 0; by < selectedBuilding.getHeight(); by++) {
                int rectX = (buildingStartX + bx) * gp.gameMap.getTileWidth() - gp.camera.x;
                int rectY = (buildingStartY + by) * gp.gameMap.getTileHeight() - gp.camera.y;
                if (gp.gameMap.getTile(bx + buildingStartX, by + buildingStartY).getProps().isBuildable()) {
                    g2.setColor(new Color(0, 255, 0, 50));
                    g2.fillRect(rectX, rectY, gp.gameMap.getTileWidth(), gp.gameMap.getTileHeight());
                } else {
                    g2.setColor(new Color(255, 0, 0, 50));
                    g2.fillRect(rectX, rectY, gp.gameMap.getTileWidth(), gp.gameMap.getTileHeight());
                }
            }
        }
        g2.setColor(Color.WHITE);
        g2.drawString("ESC", gp.mouseH.currentMousePosition.x + 15, gp.mouseH.currentMousePosition.y + 25);
        g2.drawString("ENTER", gp.mouseH.currentMousePosition.x - 50, gp.mouseH.currentMousePosition.y + 25);
    }
}

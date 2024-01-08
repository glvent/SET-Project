package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.GameObject;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.utils.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public abstract class Building extends GameObject {
    protected int level = 1; // **
    protected static String description;
    protected BuildingType props;
    protected long lastUpgradeTime; // **
    protected double accumulatedUpgradeTime; // **
    protected boolean isModalOpen = false;
    protected static int LEVEL_CAP;
    protected static final int MODAL_WIDTH = 400;
    protected static final int MODAL_HEIGHT = 250;
    protected static final int MODAL_X = (GamePanel.SCREEN_WIDTH - MODAL_WIDTH) / 2;
    protected static final int MODAL_Y = (GamePanel.SCREEN_HEIGHT - MODAL_HEIGHT) / 2;
    protected static final int BUTTON_PADDING = 10;

    // work on removing enums...
    // maybe don't remove them completely but limit them to images and name only?

    public Building(int x, int y, GamePanel gp) {
        super(x, y, gp);
        lastUpgradeTime = System.currentTimeMillis();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        if (bounds.contains(gp.mouseH.cameraRelativeMousePosition)) {
            g2.drawString(getProps().getName(), bounds.x, bounds.y - 15);
            g2.setFont(gp.gameFont16f);
            g2.drawString("Level " + level, bounds.x, bounds.y - 5);
            g2.setFont(gp.gameFont24f);
        }

        if (bounds.contains(gp.mouseH.cameraRelativeClick)) {
            isModalOpen = true;

            gp.mouseH.resetClick();
        }

        if (isModalOpen) {
            renderModal(g2);
        } else {
            if (accumulatedUpgradeTime > 0) {
                renderProgressBar(g2, getUpgradeProgress());
            } else {
                renderBuildingGameAdditions(g2);
            }
        }
    }

    public void renderProgressBar(Graphics2D g2, double progress) {
        int x = bounds.x;
        int y = bounds.y;
        int width = bounds.width;
        int height = 10;

        g2.setColor(Color.GRAY);
        g2.fillRect(x, y - height - 5, width, height);

        g2.setColor(Color.GREEN);
        g2.fillRect(x, y - height - 5, (int) (width * progress), height);
    }

    public void renderModal(Graphics2D g2) {
        if (!isModalOpen) return;

        g2.setColor(new Color(50, 50, 50, 180));
        g2.fillRoundRect(MODAL_X, MODAL_Y, MODAL_WIDTH, MODAL_HEIGHT, 10, 10);

        renderExitButton(g2);
        renderUpgradeButton(g2);
        renderBuildingModalAdditions(g2);

        g2.setFont(gp.gameFont36f);
        String title = props.getName();
        FontMetrics fmTitle = g2.getFontMetrics();
        int titleHeight = fmTitle.getHeight();
        int titleTextX = MODAL_X + 25;
        int titleTextY = MODAL_Y + titleHeight + 10;
        g2.drawString(title, titleTextX, titleTextY);

        g2.setFont(gp.gameFont24f);
        FontMetrics fmDesc = g2.getFontMetrics();
        int descLineHeight = fmDesc.getHeight();
        int descTextY = titleTextY + titleHeight;
        ArrayList<String> lines = Util.wrapText(description, fmDesc, MODAL_WIDTH - 50);

        for (String line : lines) {
            g2.drawString(line, titleTextX, descTextY);
            descTextY += descLineHeight;
        }
    }

    private void renderExitButton(Graphics2D g2) {
        final int BUTTON_SIZE = 20;

        int buttonX = MODAL_X + MODAL_WIDTH - BUTTON_SIZE - BUTTON_PADDING;
        int buttonY = MODAL_Y + BUTTON_PADDING;
        Rectangle closeButtonBounds = new Rectangle(buttonX, buttonY, BUTTON_SIZE, BUTTON_SIZE);

        if (gp.mouseH.guiRelativeMousePosition != null && closeButtonBounds.contains(gp.mouseH.guiRelativeMousePosition)) {
            g2.setColor(Color.RED.darker());
        } else {
            g2.setColor(Color.RED);
        }

        g2.fill(closeButtonBounds);
        g2.setColor(Color.WHITE);
        g2.drawString("X", buttonX + 6, buttonY + 15);

        if (isModalOpen && closeButtonBounds.contains(gp.mouseH.guiRelativeClick)) {
            isModalOpen = false;

            gp.mouseH.resetClick();
        }
    }

    private void renderUpgradeButton(Graphics2D g2) {
        final int BUTTON_WIDTH = 75;
        final int BUTTON_HEIGHT = 20;

        int buttonX = MODAL_X + MODAL_WIDTH - BUTTON_WIDTH - BUTTON_PADDING;
        int buttonY = MODAL_Y + MODAL_HEIGHT - BUTTON_HEIGHT - BUTTON_PADDING;

        Rectangle upgradeButtonBounds = new Rectangle(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        if (gp.mouseH.guiRelativeMousePosition != null && upgradeButtonBounds.contains(gp.mouseH.guiRelativeMousePosition)) {
            g2.setColor(Color.ORANGE.darker());
        } else {
            g2.setColor(Color.ORANGE);
        }

        g2.fill(upgradeButtonBounds);
        g2.setColor(Color.WHITE);

        String buttonText = "Upgrade";
        g2.setFont(gp.gameFont16f);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(buttonText);
        int textAscent = fm.getAscent();
        int textDescent = fm.getDescent();

        int textX = buttonX + (BUTTON_WIDTH - textWidth) / 2;
        int textY = buttonY + (BUTTON_HEIGHT - (textAscent + textDescent)) / 2 + textAscent;

        g2.drawString(buttonText, textX, textY);

        g2.drawString(buttonText, textX, textY);
        g2.setFont(gp.gameFont24f);

        if (upgradeButtonBounds.contains(gp.mouseH.guiRelativeClick)) {
            upgrade();

            gp.mouseH.resetClick();
        }
    }

    public void upgrade() {
        if (level < LEVEL_CAP) {
            Map<ResourceType, Integer> upgradeCost = getUpgradeCost();
            if (canAffordUpgrade(upgradeCost)) {
                consumeOnUpgrade(upgradeCost);
                startUpgradeProgress();
            }
        }
    }

    private void startUpgradeProgress() {
        if (accumulatedUpgradeTime == 0) {
            lastUpgradeTime = System.currentTimeMillis();
            accumulatedUpgradeTime = 1;
        }
    }

    private void handleUpgradeProgress() {
        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastUpgradeTime;

        double elapsedSeconds = timeElapsed / 1000.0;
        accumulatedUpgradeTime += elapsedSeconds;

        // upgrade complete
        if (accumulatedUpgradeTime >= getUpgradeTime()) {
            accumulatedUpgradeTime = 0;
            changeStatsOnUpgrade();
            level++;
        }

        lastUpgradeTime = currentTime;
    }


    public void update() {
        if (accumulatedUpgradeTime > 0) {
            handleUpgradeProgress();
        } else {
            handleBuildingSpecificUpdates();
        }
    }

    public double getUpgradeProgress() {
        double totalUpgradeTime = getUpgradeTime();
        return Math.min(accumulatedUpgradeTime / totalUpgradeTime, 1.0);
    }


    private boolean canAffordUpgrade(Map<ResourceType, Integer> upgradeCost) {
        for (Map.Entry<ResourceType, Integer> costEntry : upgradeCost.entrySet()) {
            ResourceType resourceType = costEntry.getKey();
            int requiredAmount = costEntry.getValue();
            int currentAmount = gp.resourceInventory.getResourceAmt(resourceType);

            if (currentAmount < requiredAmount) {
                return false;
            }
        }

        return true;
    }

    private void consumeOnUpgrade(Map<ResourceType, Integer> upgradeCost) {
        for (Map.Entry<ResourceType, Integer> costEntry : upgradeCost.entrySet()) {
            ResourceType resourceType = costEntry.getKey();
            int requiredAmount = costEntry.getValue();

            gp.resourceInventory.consumeResource(resourceType, requiredAmount);
        }
    }

    protected abstract Map<ResourceType, Integer> getUpgradeCost();

    protected abstract int getUpgradeTime();

    protected abstract void changeStatsOnUpgrade();

    protected abstract void renderBuildingGameAdditions(Graphics2D g2);

    protected abstract void renderBuildingModalAdditions(Graphics2D g2);

    protected abstract void handleBuildingSpecificUpdates();

    public boolean getIsModalOpen() {
        return isModalOpen;
    }

    public BuildingType getProps() {
        return props;
    }
}

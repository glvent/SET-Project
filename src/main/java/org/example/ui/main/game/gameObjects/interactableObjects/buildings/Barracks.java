package org.example.ui.main.game.gameObjects.interactableObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.entities.Friendly;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceType;
import org.example.ui.main.map.GameMap;

import java.awt.*;
import java.util.Map;

public class Barracks extends Building {
    private int troopCap = 5;
    private int troopsToTrain = 0; // **
    private final Map.Entry<ResourceType, Integer> troopCost = Map.entry(ResourceType.STEAM, 10);
    private int timeToTrain = 1;
    private long lastTrainTime; // **
    private double accumulatedTrainTime; // **
    private boolean isTraining = false; // **
    private Rectangle trainButtonBounds; // needs to be set with image values

    public Barracks(int x, int y, GamePanel gp) {
        super(x, y, gp);

        description =  "A hub for military might, the Barracks trains " +
                "your bravest citizens into skilled warriors. Upgrade to " +
                "bolster your defenses, unlock advanced units, and prepare " +
                "for any threats that dare challenge your domain.";

        LEVEL_CAP = 4;
        props = BuildingType.BARRACKS;
        bounds.setSize(props.getDimensions().width * GameMap.TILE_SIZE, props.getDimensions().height * GameMap.TILE_SIZE);
    }

    private void renderTrainButton(Graphics2D g2) {
        final int BUTTON_WIDTH = 50;
        final int BUTTON_HEIGHT = 20;

        int buttonX = MODAL_X + BUTTON_PADDING;
        int buttonY = MODAL_Y + MODAL_HEIGHT - BUTTON_HEIGHT - BUTTON_PADDING;

        trainButtonBounds = new Rectangle(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        if (gp.mouseH.guiRelativeMousePosition != null && trainButtonBounds.contains(gp.mouseH.guiRelativeMousePosition)) {
            g2.setColor(Color.ORANGE.darker());
        } else {
            g2.setColor(Color.ORANGE);
        }

        g2.fill(trainButtonBounds);
        g2.setColor(Color.WHITE);

        String buttonText = "Train";
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

        if (trainButtonBounds.contains(gp.mouseH.guiRelativeClick)) {
            gp.mouseH.resetClick();

            isModalOpen = false;
            trainTroops();
        }
    }

    private void renderTrainTroopCounter(Graphics2D g2) {
        final int ARROW_BUTTON_SIZE = 20;
        final int NUMBER_DISPLAY_WIDTH = ARROW_BUTTON_SIZE;

        int upButtonX = trainButtonBounds.x + trainButtonBounds.width + BUTTON_PADDING;
        int upButtonY = trainButtonBounds.y;
        Rectangle upArrowBounds = new Rectangle(upButtonX, upButtonY, ARROW_BUTTON_SIZE, ARROW_BUTTON_SIZE);

        int numberDisplayX = upButtonX + ARROW_BUTTON_SIZE;
        int numberDisplayY = upButtonY;
        Rectangle numberDisplayBounds = new Rectangle(numberDisplayX, numberDisplayY, NUMBER_DISPLAY_WIDTH, ARROW_BUTTON_SIZE);

        int downButtonX = numberDisplayX + NUMBER_DISPLAY_WIDTH;
        int downButtonY = upButtonY;
        Rectangle downArrowBounds = new Rectangle(downButtonX, downButtonY, ARROW_BUTTON_SIZE, ARROW_BUTTON_SIZE);

        g2.setColor(troopsToTrain < troopCap ? upArrowBounds.contains(gp.mouseH.guiRelativeMousePosition) ? Color.GRAY : Color.WHITE : Color.DARK_GRAY);
        g2.fill(upArrowBounds);

        g2.setColor(Color.WHITE);
        g2.fillRect(numberDisplayBounds.x, numberDisplayBounds.y, numberDisplayBounds.width, numberDisplayBounds.height);

        g2.setColor(troopsToTrain > 0 ? downArrowBounds.contains(gp.mouseH.guiRelativeMousePosition) ? Color.GRAY : Color.WHITE : Color.DARK_GRAY);
        g2.fill(downArrowBounds);

        Font originalFont = g2.getFont();
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(Color.BLACK);
        g2.drawString("˄", upButtonX + 5, upButtonY + 15);
        g2.drawString("˅", downButtonX + 5, downButtonY + 15);

        String troopCountStr = String.valueOf(troopsToTrain);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(troopCountStr);
        int textHeight = fm.getAscent();
        int textX = numberDisplayBounds.x + (numberDisplayBounds.width - textWidth) / 2;
        int textY = numberDisplayBounds.y + (numberDisplayBounds.height + textHeight) / 2;
        g2.drawString(troopCountStr, textX, textY);

        g2.setFont(originalFont);

        handleArrowClicks(upArrowBounds, downArrowBounds);

        g2.setColor(Color.WHITE);
    }

    private void handleArrowClicks(Rectangle upArrowBounds, Rectangle downArrowBounds) {
        if (upArrowBounds.contains(gp.mouseH.guiRelativeClick) && troopsToTrain < troopCap) {
            troopsToTrain++;
            gp.mouseH.guiRelativeClick.setLocation(-1, -1);
        }
        if (downArrowBounds.contains(gp.mouseH.guiRelativeClick) && troopsToTrain > 0) {
            troopsToTrain--;
            gp.mouseH.guiRelativeClick.setLocation(-1, -1);
        }
    }

    private void trainTroops() {
        if (troopsToTrain > 0 && !isTraining) {
            startTraining();
        }
    }

    private void startTraining() {
        lastTrainTime = System.currentTimeMillis();
        accumulatedTrainTime = 0;
        isTraining = true;
    }

    private void handleTrainingProgress() {
        if (!isTraining) return;

        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - lastTrainTime;

        double elapsedSeconds = timeElapsed / 1000.0;
        accumulatedTrainTime += elapsedSeconds;

        if (accumulatedTrainTime >= timeToTrain) {
            accumulatedTrainTime = 0;
            troopsToTrain--;
            Friendly troop = new Friendly(new Rectangle(bounds.x, bounds.y - 25, 25, 25), gp);
            gp.resourceInventory.consumeResource(troopCost);
            gp.addEntity(troop);
            if (troopsToTrain > 0) {
                startTraining();
            } else {
                isTraining = false;
            }
        }

        lastTrainTime = currentTime;
    }

    public void renderTrainingProgressBar(Graphics2D g2) {
        if (!isTraining) return;

        int x = bounds.x;
        int y = bounds.y + bounds.height + 10;
        int width = bounds.width;
        int height = 10;

        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, width, height);

        double progress = getTrainingProgress();
        g2.setColor(Color.BLUE);
        g2.fillRect(x, y, (int) (width * progress), height);
    }

    private double getTrainingProgress() {
        return Math.min(accumulatedTrainTime / timeToTrain, 1.0);
    }

    @Override
    protected Map<ResourceType, Integer> getUpgradeCost() {
        switch (level) {
            case 1 -> {
                return Map.of(
                        ResourceType.COPPER, 100,
                        ResourceType.COGS, 100,
                        ResourceType.STEAM, 50
                );
            }
            case 2 -> {
                return Map.of(
                        ResourceType.COPPER, 200,
                        ResourceType.COGS, 200,
                        ResourceType.STEAM, 100
                );
            }
            case 3 -> {
                return Map.of(
                        ResourceType.COPPER, 500,
                        ResourceType.COGS, 500,
                        ResourceType.STEAM, 250
                );
            }
        }
        return null;
    }

    @Override
    protected int getUpgradeTime() {
        switch (level) {
            case 1 -> {
                return 20;
            }
            case 2 -> {
                return 60;
            }
            case 3 -> {
                return 200;
            }
        }
        return 0;
    }

    @Override
    protected void changeStatsOnUpgrade() {
        switch (level) {
            case 1 -> {
                troopCap = 6;
                timeToTrain = 25;
            }
            case 2 -> {
                troopCap = 7;
                timeToTrain = 20;
            }
            case 3 -> {
                troopCap = 8;
                timeToTrain = 15;
            }
        }
    }

    @Override
    protected void renderBuildingGameAdditions(Graphics2D g2) {
        renderTrainingProgressBar(g2);
    }

    @Override
    protected void renderBuildingModalAdditions(Graphics2D g2) {
        renderTrainButton(g2);
        renderTrainTroopCounter(g2);
    }

    @Override
    protected void handleBuildingSpecificUpdates() {
        handleTrainingProgress();
    }

}

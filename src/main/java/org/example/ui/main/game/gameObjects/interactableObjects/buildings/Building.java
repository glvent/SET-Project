package org.example.ui.main.game.gameObjects.buildings;

import org.example.ui.main.GamePanel;
import org.example.ui.main.game.gameObjects.GameObject;

import java.awt.*;

public abstract class Building extends GameObject {
    protected int level = 1;
    protected BuildingType props;
    protected boolean isModalOpen = false;
    protected static int LEVEL_CAP;
    private static final int MODAL_WIDTH = 400;
    private static final int MODAL_HEIGHT = 250;
    private static final int MODAL_X = (GamePanel.SCREEN_WIDTH - MODAL_WIDTH) / 2;
    private static final int MODAL_Y = (GamePanel.SCREEN_HEIGHT - MODAL_HEIGHT) / 2;

    // work on removing enums...
    // maybe don't remove them completely but limit them to images and name only?

    public Building(int x, int y, GamePanel gp) {
        super(x, y, gp);
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

        if (bounds.contains(gp.mouseH.cameraRelativeMousePosition)) {
            g2.setFont(gp.gameFont16f);
            g2.drawString(props.getName() + " LvL: " + level, bounds.x, bounds.y);
            g2.setFont(gp.gameFont24f);
        }

        if (bounds.contains(gp.mouseH.cameraRelativeClick)) {
            isModalOpen = true;
            gp.mouseH.guiRelativeClick.setLocation(-1, -1);
        }

        if (isModalOpen) {
            renderModal(g2);
        }
    }

    public void renderModal(Graphics2D g2) {
        final int BUTTON_SIZE = 20;
        final int BUTTON_PADDING = 5;

        g2.setColor(new Color(50, 50, 50, 180));
        g2.fillRoundRect(MODAL_X, MODAL_Y, MODAL_WIDTH, MODAL_HEIGHT, 10, 10);

        // turn into a component?

        int buttonX = MODAL_X + MODAL_WIDTH - BUTTON_SIZE - BUTTON_PADDING;
        int buttonY = MODAL_Y + BUTTON_PADDING;
        Rectangle closeButtonBounds = new Rectangle(buttonX, buttonY, BUTTON_SIZE, BUTTON_SIZE);

        if (gp.mouseH.guiRelativeMousePostion != null && closeButtonBounds.contains(gp.mouseH.guiRelativeMousePostion)) {
            g2.setColor(Color.RED.darker());
        } else {
            g2.setColor(Color.RED);
        }

        g2.fill(closeButtonBounds);
        g2.setColor(Color.WHITE);
        g2.drawString("X", buttonX + 6, buttonY + 15);

        renderUpgradeButton(g2);

        if (isModalOpen && closeButtonBounds.contains(gp.mouseH.guiRelativeClick)) {
            isModalOpen = false;
        }

    }

    public void renderUpgradeButton(Graphics2D g2) {
        final int BUTTON_PADDING = 5;
        final int BUTTON_WIDTH = 75;
        final int BUTTON_HEIGHT = 20;

        int buttonX = MODAL_X + MODAL_WIDTH - BUTTON_WIDTH - BUTTON_PADDING;
        int buttonY = MODAL_Y + MODAL_HEIGHT -  BUTTON_HEIGHT - BUTTON_PADDING;

        Rectangle upgradeButtonBounds = new Rectangle(buttonX, buttonY, BUTTON_WIDTH, BUTTON_HEIGHT);

        if (gp.mouseH.guiRelativeMousePostion != null && upgradeButtonBounds.contains(gp.mouseH.guiRelativeMousePostion)) {
            g2.setColor(Color.ORANGE.darker());
        } else {
            g2.setColor(Color.ORANGE);
        }

        g2.fill(upgradeButtonBounds);
        g2.setColor(Color.WHITE);

        String buttonText = "Upgrade";
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(buttonText);
        int textHeight = fm.getAscent();

        int textX = buttonX + (BUTTON_WIDTH - textWidth) / 2;
        int textY = buttonY + (BUTTON_HEIGHT + textHeight) / 2;

        g2.setFont(gp.gameFont16f);
        g2.drawString(buttonText, textX, textY);
        g2.setFont(gp.gameFont24f);

        if (upgradeButtonBounds.contains(gp.mouseH.guiRelativeClick)) {
            upgrade();

            gp.mouseH.guiRelativeClick.setLocation(-1, -1);
        }
    }

    public void upgrade() {
        if (level < LEVEL_CAP) {
            level++;
            onUpgrade();
        }
    };

    protected abstract void onUpgrade();

    public boolean getIsModalOpen() {
        return isModalOpen;
    }

    public BuildingType getProps() {
        return props;
    }
}

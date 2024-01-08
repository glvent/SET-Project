package org.example.ui.main.game.gameInterface;

import org.example.ui.input.KeyHandler;
import org.example.ui.main.GamePanel;
import org.example.ui.utils.Util;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Dialogue {
    private static class Step {
        String text;
        Polygon direction;

        Step(String text, Polygon direction) {
            this.text = text;
            this.direction = direction;
        }
    }

    private ArrayList<Step> steps; // **
    private int currentStepIndex; // **
    private int charIndex = 0;
    private long lastCharTime;
    private static final int CHAR_TYPE_DELAY = 75;
    private static final int ARROW_SIZE = 20;
    private boolean isVisible;
    private GamePanel gp;

    public Dialogue(GamePanel gp) {
        this.gp = gp;
        this.steps = new ArrayList<>();
        this.isVisible = false;
        this.currentStepIndex = 0;
        this.lastCharTime = System.currentTimeMillis();
    }

    public void addStep(String text, Polygon direction) {
        steps.add(new Step(text, direction));
    }

    public void nextStep() {
        if (!isVisible) return;

        if (currentStepIndex < steps.size() - 1) {
            currentStepIndex++;
            charIndex = 0;
        } else {
            reset(); // hide and clear after last step
        }
    }

    public void show() {
        if (!steps.isEmpty()) {
            isVisible = true;
        }
    }

    public void reset() {
        currentStepIndex = 0;
        charIndex = 0;
        steps.clear();
        isVisible = false;
    }

    public void update() {
        if (!isVisible || charIndex >= steps.get(currentStepIndex).text.length()) {
            if (gp.keyH.getCurrentKeyEvent() == KeyEvent.VK_SPACE) {
                nextStep();
            }
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCharTime >= CHAR_TYPE_DELAY) {
            charIndex++;
            lastCharTime = currentTime;
        }
    }

    public void render(Graphics2D g2) {
        if (!isVisible) return;

        Step currentStep = steps.get(currentStepIndex);

        int x = 100;
        int y = gp.getHeight() - 100;
        int width = 600;
        int height = 100;

        g2.setColor(new Color(20, 20, 20, 50));
        g2.fillRoundRect(x, y, width, height, 10, 10);

        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        ArrayList<String> lines = Util.wrapText(currentStep.text.substring(0, charIndex), fm, width - 50);

        int lineHeight = fm.getHeight();
        int textY = y + 25;
        for (String line : lines) {
            g2.drawString(line, x + 25, textY);
            textY += lineHeight;
        }


        if (currentStep.direction == null) return;
        g2.setColor(Color.ORANGE);
        g2.fillPolygon(currentStep.direction);
    }

    public void initDialogue() {
        addStep("Welcome fellow steampunker!", null);
        addStep("Let me show you around...", null);
        addStep("This here is your shop where you will expand your steam empire.",
                createArrow(gp.builder.getBounds().x + (gp.builder.getBounds().width / 2), gp.builder.getBounds().y - 10, 'd'));

        show();
    }

    private Polygon createArrow(int x, int y, char direction) {
        Polygon arrow = new Polygon();

        switch (direction) {
            case 'l':
                arrow.addPoint(x, y);
                arrow.addPoint(x + ARROW_SIZE, y - ARROW_SIZE / 2);
                arrow.addPoint(x + ARROW_SIZE, y + ARROW_SIZE / 2);
                break;
            case 'r':
                arrow.addPoint(x, y - ARROW_SIZE / 2);
                arrow.addPoint(x, y + ARROW_SIZE / 2);
                arrow.addPoint(x + ARROW_SIZE, y);
                break;
            case 'u':
                arrow.addPoint(x - ARROW_SIZE / 2, y);
                arrow.addPoint(x + ARROW_SIZE / 2, y);
                arrow.addPoint(x, y - ARROW_SIZE);
                break;
            case 'd':
                arrow.addPoint(x - ARROW_SIZE / 2, y - ARROW_SIZE);
                arrow.addPoint(x + ARROW_SIZE / 2, y - ARROW_SIZE);
                arrow.addPoint(x, y);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        return arrow;
    }
}

package org.example.ui.main.gameObjects.gameInterface;

import org.example.ui.input.KeyHandler;
import org.example.ui.main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Dialogue {
    private ArrayList<String> parts;
    private int currentPartIndex;
    private int charIndex;
    private String currentText;
    private long lastCharTime;
    private static final long CHAR_TYPE_DELAY = 75;
    private boolean isVisible;
    private GamePanel gp;
    private KeyHandler keyH;

    public Dialogue(KeyHandler keyH, GamePanel gp) {
        this.gp = gp;
        this.keyH = keyH;
        this.parts = new ArrayList<>();
        this.isVisible = false;
        this.currentPartIndex = 0;
        this.charIndex = 0;
        this.lastCharTime = System.currentTimeMillis();
    }

    public void addPart(String text) {
        parts.add(text);
    }

    public void nextPart() {
        if (!isVisible) return;

        if (currentPartIndex < parts.size() - 1) {
            currentPartIndex++;
            currentText = parts.get(currentPartIndex);
            charIndex = 0;
        } else {
            reset(); // hide and clear after last part
        }
    }

    public void show() {
        if (!parts.isEmpty()) {
            currentText = parts.get(currentPartIndex);
            isVisible = true;
        }
    }

    public void hide() {
        isVisible = false;
    }

    public void reset() {
        currentText = null;
        currentPartIndex = 0;
        charIndex = 0;
        parts.clear();
        isVisible = false;
    }

    public void update() {
        if (!isVisible || charIndex >= currentText.length()) {
            if (keyH.getCurrentKeyEvent() == KeyEvent.VK_SPACE) {
                nextPart();
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

        int x = 100;
        int y = gp.getHeight() - 100;
        int width = 600;
        int height = 100;

        g2.setColor(new Color(20, 20, 20, 50));
        g2.fillRoundRect(x, y, width, height, 10, 10);

        g2.setColor(Color.WHITE);
        g2.drawString(currentText.substring(0, charIndex), x + 25, y + 25);
    }

    public void initDialogue() {
        addPart("Welcome fellow steampunker!");
        addPart("Let me show you around...");

        this.show();
    }
}

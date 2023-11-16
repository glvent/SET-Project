package org.example.ui.main;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    Game game;

    public GamePanel(Game game) {
        this.game = game;
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(50, 50, 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.gsm.render(g);
        // draw fps to screen
        g.drawString("FPS: " + game.SHOWN_FPS, 125, 10);
    }

    public void addToPanel(JComponent[] components) {
        for (int i = 0; i < components.length; i++) {
            this.add(components[i]);
        }
    }

}

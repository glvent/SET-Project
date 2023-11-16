package org.example.ui.main;

import javax.swing.*;

public class Game extends JFrame implements Runnable {
    public GamePanel gamePanel;
    volatile int FPS = 0;
    int SHOWN_FPS = 0;

    GameStateManager gsm;

    public Game() {
        gamePanel = new GamePanel(this);
        this.add(gamePanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(800, 600);
        this.setVisible(true);

        gsm = new GameStateManager(gamePanel);
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        int FPS_LIMIT = 120;
        double CALC_FPS_LIMIT = 1000000000D / ((double) FPS_LIMIT);

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / CALC_FPS_LIMIT;
            lastTime = now;

            boolean shouldRender = false;

            while (delta >= 1) {
                update();
                delta -= 1;
                shouldRender = true;
            }

            if (shouldRender) {
                FPS++;
                gamePanel.repaint();
            }


            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                SHOWN_FPS = FPS;
                FPS = 0;
            }
        }
    }

    private void update() {
        gsm.update();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Game game = new Game(); // Create and show the game UI
                // Start the game loop in a separate thread
                new Thread(game).start();
            }
        });
    }
}

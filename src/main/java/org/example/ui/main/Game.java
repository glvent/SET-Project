package org.example.ui.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Game implements Runnable {
    public GameFrame gf;
    public GamePanel gp;
    Thread gameThread;
    int SHOWN_FPS = 0; // for logging
    long startTime;
    long closeTime;

    public Game() {
        gp = new GamePanel(this);
        gf = new GameFrame(this);
        setupExitListener();
    }

    private void setupExitListener() {
        gf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeGame();
            }
        });
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.startGame();
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
        startTime = System.currentTimeMillis();
    }

    public void closeGame() {
        System.out.println("Game closing...");
        closeTime = System.currentTimeMillis();
        gameThread = null;
        System.exit(0);
    }

    @Override
    public void run() {
        int FPS_LIMIT = 120;
        double drawInterval = 1000000000d / FPS_LIMIT;
        double delta = 0;
        double lastTime = System.nanoTime();
        double currentTime;
        long timer = 0;
        int frameCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta > 0) {
                gp.update();
                gp.repaint();
                delta--;
                frameCount++;
            }

            if (timer >= 1000000000) {
                SHOWN_FPS = frameCount;
                frameCount = 0;
                timer = 0;
                System.out.println(".");
            }
        }
    }
}

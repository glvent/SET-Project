package org.example.ui.main;

public class Game implements Runnable {
    public GameFrame gf;
    public GamePanel gp;
    Thread gameThread;
    int SHOWN_FPS = 0; // for logging

    public Game() {
        gp = new GamePanel(this);
        gf = new GameFrame(this);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.startGameThread();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
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

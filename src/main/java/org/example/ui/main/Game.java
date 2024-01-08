package org.example.ui.main;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;

public class Game implements Runnable {
    private GameFrame gf;
    private GamePanel gp;
    private Thread gameThread;
    public int SHOWN_FPS = 0; // for logging
    private long startTime;
    private long closeTime;
    private GameState currentState;

    private enum GameState {
        HOME, PAUSED, PLAYING
    }

    public Game() {
        gp = new GamePanel(this);
        gf = new GameFrame(gp);
        setupExitListener();
        currentState = GameState.HOME;
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

    public static void initializeFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("env.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId("set-project-ec576")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (gameThread != null) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                switch (currentState) {
                    case PLAYING:
                        updatePlaying();
                        break;
                    case PAUSED:
                        updatePaused();
                        break;
                    case HOME:
                        updateHome();
                        break;
                }
                delta--;
            }
            if (gameThread != null) {
                render();
                frames++;

                if (System.currentTimeMillis() - timer > 1000) {
                    timer += 1000;
                    SHOWN_FPS = frames;
                    frames = 0;
                }
            }
        }
    }

    private void updatePlaying() {

    }

    private void updatePaused() {

    }

    private void updateHome() {

    }

    private void render() {

    }

    // State transition methods
    public void pauseGame() {
        currentState = GameState.PAUSED;
    }

    public void resumeGame() {
        currentState = GameState.PLAYING;
    }

    public void goToHome() {
        currentState = GameState.HOME;
    }


}

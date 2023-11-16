package org.example.ui.main;

import org.example.ui.state.GameState;
import org.example.ui.state.InGameState;
import org.example.ui.state.MainMenuState;
import org.example.ui.state.PauseState;

import java.awt.*;

public class GameStateManager {
    private GameState currentState;
    MainMenuState mainMenuState;
    PauseState pausedState;
    InGameState inGameState;
    public GamePanel panel;

    public GameStateManager(GamePanel panel) {
        this.panel = panel;
        mainMenuState = new MainMenuState(this);
        pausedState = new PauseState(this);
        inGameState = new InGameState(this);

        // initialize a main menu
        // not sure if this is good practice...
        setState(mainMenuState);
    }

    public void setState(GameState newState) {
        if (currentState != null) {
            System.out.println("State changed from " +
                    this.currentState.getClass().getSimpleName() +
                    " to " + newState.getClass().getSimpleName());
            onExit();

            currentState = newState;

            onEnter();
        } else {
            System.out.println("New state created");
            currentState = newState;
            onEnter();
        }
    }

    void update() {
        currentState.update();
    }

    void render(Graphics g) {
        currentState.render(g);
    }

    void handleInput() {
        currentState.handleInput();
    }

    void onEnter() {
        currentState.onEnter();
    }

    void onExit() {
        currentState.onExit();
    }

    public InGameState getInGameState() {
        return inGameState;
    }

    public MainMenuState getMainMenuState() {
        return mainMenuState;
    }

    public PauseState getPausedState() {
        return pausedState;
    }

    public GameState getCurrentState() {
        return currentState;
    }
}

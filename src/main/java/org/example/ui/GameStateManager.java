package org.example.ui.state;

import java.awt.*;

public class GameStateManager {
    private GameState currentState;

    public GameStateManager() {
        // initialize a main menu
        currentState = new MainMenuState();
    }

    void set_state(GameState currentState) {
        this.currentState = currentState;
    }

    void update() {
        currentState.update();
    }

    void render(Graphics g) {
        currentState.render(g);
    }

    void handle_input() {
        currentState.handle_input();
    }
}

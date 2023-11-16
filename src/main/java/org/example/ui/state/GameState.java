package org.example.ui;

public interface GameState {
    void update();
    void render();
    void handle_input();
}

package org.example.ui.state;

import java.awt.*;

public interface GameState {
    void update();
    void render(Graphics g);
    void handleInput();
    void onEnter();
    void onExit();
}

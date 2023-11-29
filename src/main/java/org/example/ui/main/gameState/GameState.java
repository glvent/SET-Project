package org.example.ui.main;

public abstract class GameState {
    protected GameStateManager gsm;

    public abstract void update();
    public abstract void render();
    public abstract void onEnter();
    public abstract void onExit();
}

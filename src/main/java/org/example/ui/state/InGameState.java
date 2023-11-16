package org.example.ui.state;

import org.example.ui.main.GameStateManager;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InGameState implements GameState {
    GameStateManager gsm;

    public InGameState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("IN_GAME_STATE", 10, 10);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void onEnter() {
        System.out.println("Entered in-game state");
        gsm.panel.requestFocusInWindow();
        KeyListener kl = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    gsm.setState(gsm.getPausedState());
                }
            }
        };

        gsm.panel.addKeyListener(kl);
        gsm.panel.setFocusable(true);

    }

    @Override
    public void onExit() {
        System.out.println("Exited game state");

        gsm.panel.revalidate();
    }
}

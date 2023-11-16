package org.example.ui.state;

import org.example.ui.main.GameStateManager;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static org.example.ui.utils.DrawUtils.drawCenteredString;

public class PauseState implements GameState {
    GameStateManager gsm;

    public PauseState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("PAUSED_STATE", 10, 10);
        drawCenteredString(g, "PAUSED", 36, 0, 0, gsm.panel.getWidth(), gsm.panel.getHeight());
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void onEnter() {
        System.out.println("Entered pause state");

        gsm.panel.requestFocusInWindow();
        KeyListener kl = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.out.println("unpaused");
                    gsm.setState(gsm.getInGameState());
                }
            }
        };

        gsm.panel.addKeyListener(kl);
        gsm.panel.setFocusable(true);

    }

    @Override
    public void onExit() {
        System.out.println("Exited pause state");

    }
}

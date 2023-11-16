package org.example.ui.state;

import org.example.ui.main.GameStateManager;

import javax.swing.*;
import java.awt.*;

public class MainMenuState implements GameState {
    GameStateManager gsm;
    JButton startBtn;

    public MainMenuState(GameStateManager gsm) {
        this.gsm = gsm;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("MAIN_MENU", 10, 10);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void onEnter() {
        System.out.println("Entered game menu state");

        gsm.panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        createStartButton();
        gsm.panel.addToPanel(new JComponent[]{startBtn});
        gsm.panel.revalidate();
    }

    @Override
    public void onExit() {
        System.out.println("Exited game menu state");

        gsm.panel.remove(startBtn);
        gsm.panel.revalidate();
    }

    private void createStartButton() {
        startBtn = new JButton("Start Game");
        startBtn.addActionListener(e -> {
            System.out.println("Game started!");
            gsm.setState(gsm.getInGameState());
        });

        startBtn.setContentAreaFilled(true);
        startBtn.setMargin(new Insets(10, 10, 10, 10));
    }
}

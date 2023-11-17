package org.example.ui.main;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame(Game game) {
        this.add(game.gp);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

}

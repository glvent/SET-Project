package org.example.ui.main;

import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame(GamePanel gp) {
        this.add(gp);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
    }

}

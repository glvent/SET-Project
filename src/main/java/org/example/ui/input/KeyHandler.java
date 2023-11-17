package org.example.ui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public char keyPressed = ' ';

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> keyPressed = 'W';
            case KeyEvent.VK_A -> keyPressed = 'A';
            case KeyEvent.VK_S -> keyPressed = 'S';
            case KeyEvent.VK_D -> keyPressed = 'D';
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed = ' ';
    }
}

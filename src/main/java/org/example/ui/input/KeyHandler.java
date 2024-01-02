package org.example.ui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.HashSet;
import java.util.Set;

public class KeyHandler implements KeyListener {
    private final Set<Character> keysPressed = new HashSet<>();
    private int currentKeyCode;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(Character.toUpperCase(e.getKeyChar()));
        currentKeyCode = e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove(Character.toUpperCase(e.getKeyChar()));
        currentKeyCode = KeyEvent.VK_UNDEFINED;
    }

    public boolean isKeyPressed(char key) {
        return keysPressed.contains(Character.toUpperCase(key));
    }

    public int getCurrentKeyEvent() {
        return currentKeyCode != KeyEvent.VK_UNDEFINED ? currentKeyCode : KeyEvent.VK_UNDEFINED;
    }
}

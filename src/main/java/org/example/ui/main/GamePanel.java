package org.example.ui.main;

import org.example.ui.gameObjects.Enemy;
import org.example.ui.gameObjects.Player;
import org.example.ui.input.KeyHandler;
import org.example.ui.utils.Position;
import org.example.ui.utils.Vector;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private Game game;

    private Player player;
    private ArrayList<Enemy> enemies;

    KeyHandler keyIn;

    private Position camera;

    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(new Color(50, 50, 50));

        keyIn = new KeyHandler();

        addKeyListener(keyIn);
        setFocusable(true);

        player = new Player(keyIn);
        enemies = new ArrayList<>();
        camera = new Position(0, 0);

        enemies.add(new Enemy(new Position(100, 100), new Vector(3, 3), true, true, null));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);

        // translate camera
        g2.translate(-camera.x, -camera.y);

        drawPlayer(g2);
        drawEnemies(g2);

        // translate back for UI
        g2.translate(camera.x, camera.y);

        // draw fps to screen
        g2.drawString("FPS: " + game.SHOWN_FPS, 0, 15);
    }

    public void drawPlayer(Graphics2D g2) {

        g2.fillRect(player.position.x, player.position.y, 20, 20);

    }

    public void drawEnemies(Graphics2D g2) {
        g2.setColor(Color.red);
        enemies.forEach(enemy -> {
            g2.fillRect(enemy.position.x, enemy.position.y, 20, 20);
        });
        g2.setColor(Color.WHITE);
    }

    public void update() {
        player.update();
        enemies.forEach(enemy -> {
            enemy.update(player.position);
        });

        // updates camera to focus on the player
        camera.x = player.position.x - SCREEN_WIDTH / 2;
        camera.y = player.position.y - SCREEN_HEIGHT / 2;
    }

    public Player getPlayer() {
        return player;
    }
}

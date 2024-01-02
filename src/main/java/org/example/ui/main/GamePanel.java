package java.org.example.ui.main;

import org.example.ui.main.game.entities.Entity;
import org.example.ui.main.game.gameInterface.Builder;
import org.example.ui.main.game.gameInterface.Dialogue;
import org.example.ui.input.KeyHandler;
import org.example.ui.input.MouseHandler;
import org.example.ui.main.game.gameObjects.interactableObjects.buildings.Building;
import org.example.ui.main.game.gameObjects.interactableObjects.buildings.Generator;
import org.example.ui.main.game.gameObjects.interactableObjects.resources.ResourceInventory;
import org.example.ui.main.map.GameMap;
import org.example.ui.utils.Position;
import org.example.ui.utils.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GamePanel extends JPanel {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private Game game;

    public KeyHandler keyH;
    public MouseHandler mouseH;

    public Position camera;
    public static final int CAMERA_SPEED = 5;

    public GameMap gameMap;
    public Dialogue dialogue;
    public Rectangle viewport;
    private ArrayList<Entity> entities;
    public ResourceInventory resourceInventory;
    private Builder builder;
    public Font gameFont36f;
    public Font gameFont24f;
    public Font gameFont16f;

    public final Rectangle returnToBaseButton;

    public GamePanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(new Color(50, 50, 50));
        gameFont36f = Util.readFont("/gameFont.ttf", 36f);
        gameFont24f = Util.readFont("/gameFont.ttf", 24f);
        gameFont16f = Util.readFont("/gameFont.ttf", 16f);

        keyH = new KeyHandler();
        mouseH = new MouseHandler(this);

        addKeyListener(keyH);
        addMouseListener(mouseH);
        addMouseMotionListener(mouseH);
        addMouseWheelListener(mouseH);
        setFocusable(true);

        entities = new ArrayList<>();
        camera = new Position(0, 0);
        dialogue = new Dialogue(keyH, this);
        resourceInventory = new ResourceInventory(this);
        builder = new Builder(this);

        viewport =  new Rectangle(camera.x, camera.y, SCREEN_WIDTH, SCREEN_HEIGHT);
        gameMap = new GameMap(1000, 1000, this);

        returnToBaseButton = new Rectangle();

        setDoubleBuffered(true);

        dialogue.initDialogue();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.setFont(gameFont24f);

        // translate camera
        g2.translate(-camera.x, -camera.y);

        gameMap.render(g2);

        getEntities().forEach(entity -> entity.render(g2));

        // translate back for UI
        g2.translate(camera.x, camera.y);

        // draw fps to screen
        g2.drawString("FPS: " + game.SHOWN_FPS, 0, 15);

        // TODO: figure out how I can stop camera movement
        //  as well as other modals from opening when one is already open

        resourceInventory.render(g2);
        builder.render(g2);
        gameMap.renderMinimap(g2);
        dialogue.render(g2);

        // must render buildings modal outside of class because of rendering layer...
        // works fine but I want to only ever show one modal at a time anyway...?

        for (Building building : gameMap.getBuildings()) {
            if (building.getIsModalOpen()) {
                building.renderModal(g2);
            }
        }

        renderReturnToBaseButton(g2);
    }

    public void update() {
        handleCameraMovement();

        // should update things such as entity position, building timer,
        // resources gained by mine, search parties, etc...

        getEntities().forEach(Entity::update);
        gameMap.update();
        dialogue.update();

        ArrayList<Generator> generators = gameMap.getTypeOfGameObjects(Generator.class);
        generators.forEach(Generator::generate);
    }

    private void handleCameraMovement() {
        double dx = 0;
        double dy = 0;

        if (keyH.isKeyPressed('W')) {
            dy -= CAMERA_SPEED;
        }
        if (keyH.isKeyPressed('S')) {
            dy += CAMERA_SPEED;
        }
        if (keyH.isKeyPressed('A')) {
            dx -= CAMERA_SPEED;
        }
        if (keyH.isKeyPressed('D')) {
            dx += CAMERA_SPEED;
        }

        // make a normalizeVector method
        if (dx != 0 && dy != 0) {
            double distance = Position.getDistance(dx, dy, dx, dy);
            dx = (dx / distance) * CAMERA_SPEED;
            dy = (dy / distance) * CAMERA_SPEED;
        }


        boolean outerLimitX = camera.x + dx <= gameMap.MAP_WIDTH * gameMap.TILE_SIZE - SCREEN_WIDTH;
        boolean outerLimitY = camera.y + dy <= gameMap.MAP_HEIGHT * gameMap.TILE_SIZE - SCREEN_HEIGHT;
        boolean innerLimitX = camera.x + dx >= 0;
        boolean innerLimitY = camera.y + dy >= 0;

        if (outerLimitX && innerLimitX) {
            camera.x += (int) dx;
        }
        if (outerLimitY && innerLimitY) {
            camera.y += (int) dy;
        }


        viewport.setBounds(camera.x, camera.y, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    public void renderReturnToBaseButton(Graphics2D g2) {
        int buttonWidth = 120;
        int buttonHeight = 30;
        int buttonX = 100; // 20 pixels from the right edge
        int buttonY = 0; // 20 pixels from the bottom edge

        returnToBaseButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);

        if (mouseH.guiRelativeMousePostion != null && returnToBaseButton.contains(mouseH.guiRelativeMousePostion)) {
            g2.setColor(new Color(75, 75, 75, 75));
        } else {
            g2.setColor(new Color(35, 35, 35, 75));
        }

        g2.fillRoundRect(returnToBaseButton.x, returnToBaseButton.y, returnToBaseButton.width, returnToBaseButton.height, 10, 10);

        String buttonText = "Return to Base";
        Font font = Util.readFont("/gameFont.ttf", 18);
        FontMetrics metrics = g2.getFontMetrics(font);
        int textX = buttonX + (buttonWidth - metrics.stringWidth(buttonText)) / 2;
        int textY = buttonY + ((buttonHeight - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.drawString(buttonText, textX, textY);

        if (mouseH.guiRelativeClick != null && returnToBaseButton.contains(mouseH.guiRelativeClick)) {
            int baseCenterX = gameMap.getBaseCenter().x * gameMap.TILE_SIZE;
            int baseCenterY = gameMap.getBaseCenter().y * gameMap.TILE_SIZE;

            camera.x = baseCenterX - SCREEN_WIDTH / 2;
            camera.y = baseCenterY - SCREEN_HEIGHT / 2;

            camera.x = Math.max(0, Math.min(camera.x, gameMap.MAP_WIDTH * gameMap.TILE_SIZE - SCREEN_WIDTH));
            camera.y = Math.max(0, Math.min(camera.y, gameMap.MAP_HEIGHT * gameMap.TILE_SIZE - SCREEN_HEIGHT));

            mouseH.guiRelativeClick.setLocation(-1, -1);
        }
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Builder getBuilder() {
        return builder;
    }
}

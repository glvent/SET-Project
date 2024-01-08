    package org.example.ui.main.map;

    import com.raylabz.opensimplex.OpenSimplexNoise;
    import org.example.ui.main.game.entities.Enemy;
    import org.example.ui.main.game.entities.Entity;
    import org.example.ui.main.game.gameObjects.*;
    import org.example.ui.main.GamePanel;
    import org.example.ui.main.game.gameObjects.interactableObjects.buildings.Building;
    import org.example.ui.main.game.gameObjects.interactableObjects.buildings.BuildingType;
    import org.example.ui.main.game.gameObjects.interactableObjects.buildings.TownHall;
    import org.example.ui.main.game.gameObjects.interactableObjects.resources.AetherCrystal;
    import org.example.ui.main.game.gameObjects.interactableObjects.resources.CopperVein;
    import org.example.ui.main.game.gameObjects.staticObjects.Cloud;
    import org.example.ui.main.game.gameObjects.staticObjects.Decoration;
    import org.example.ui.utils.Util;

    import java.awt.*;
    import java.awt.image.BufferedImage;
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.Random;
    import java.util.Set;

    public class GameMap {
        // ** indicated that it needs to be stored in db and reinitialized

        private final Tile[][] tiles; // **
        private final ArrayList<GameObject> gameObjects; // **
        private final OpenSimplexNoise noise;
        private final Random rand = new Random();
        private final GamePanel gp;
        private final Point baseCenter; // **
        private final Set<Point> generatedChunks; // **
        public static final int TILE_SIZE = 32;
        public final int MAP_WIDTH; // width in terms of number of tiles
        public final int MAP_HEIGHT; // height in terms of number of tiles
        public static final int MINIMAP_WIDTH = 150;
        public static final int MINIMAP_HEIGHT = 150;
        public static final int MINIMAP_MARGIN = 10;

        private final BufferedImage fogTexture;

        public GameMap(int MAP_WIDTH, int MAP_HEIGHT, GamePanel gp) {
            this.MAP_WIDTH = MAP_WIDTH;
            this.MAP_HEIGHT = MAP_HEIGHT;
            this.gp = gp;

            tiles = new Tile[MAP_WIDTH][MAP_HEIGHT];
            noise = new OpenSimplexNoise(rand.nextLong());

            generatedChunks = new HashSet<>();
            gameObjects = new ArrayList<>();

            // scales fog texture for scale...
            fogTexture = Util.scaleTexture(
                    Util.readImage("/fog.png"), (TILE_SIZE * 4.0) / 256);

            generateMap();

            baseCenter = findBaseLocation();
            if (baseCenter != null) {
                int townHallWidth = BuildingType.TOWN_HALL.getDimensions().width;
                int townHallHeight = BuildingType.TOWN_HALL.getDimensions().height;

                int basePixelX = baseCenter.x * TILE_SIZE;
                int basePixelY = baseCenter.y * TILE_SIZE;

                int townHallX = baseCenter.x * TILE_SIZE - (townHallWidth * TILE_SIZE) / 2;
                int townHallY = baseCenter.y * TILE_SIZE - (townHallHeight * TILE_SIZE) / 2;

                gp.camera.x = basePixelX - GamePanel.SCREEN_WIDTH / 2;
                gp.camera.y = basePixelY - GamePanel.SCREEN_HEIGHT / 2;

                ArrayList<GameObject> gameObjectsToRemove = new ArrayList<>();

                for (GameObject gameObject : gameObjects) {
                    if (new Rectangle(townHallX, townHallY, townHallWidth, townHallHeight).intersects(gameObject.getBounds())) {
                        gameObjectsToRemove.add(gameObject);
                    }
                }

                gameObjects.removeAll(gameObjectsToRemove);

                gameObjects.add(new TownHall(townHallX, townHallY, gp));

                int initExploreRadius = 15;
                exploreTilesAround(baseCenter.x, baseCenter.y, initExploreRadius);
            }
        }

        private void generateMap() {
            // look into QuadTree approach for rendering and collision...
            double noiseScale = 0.99;

            for (int x = 0; x < MAP_WIDTH; x++) {
                for (int y = 0; y < MAP_HEIGHT; y++) {
                    double noiseValue = noise.getNoise2D(x * noiseScale, y * noiseScale).getValue();
                    TileType type = (noiseValue > 0) ? TileType.GRASS : TileType.AIR;
                    tiles[x][y] = new Tile(type, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                    Fog fogVar = generateRandomFog(x, y);
                    tiles[x][y].setFog(fogVar);
                }
            }
        }

        private void generateObjectsInViewPort() {
            int chunkSize = 10;

            int startX = Math.max(0, gp.viewport.x / TILE_SIZE / chunkSize);
            int startY = Math.max(0, gp.viewport.y / TILE_SIZE / chunkSize);
            int endX = Math.min(MAP_WIDTH / chunkSize, (gp.viewport.x + gp.viewport.width) / TILE_SIZE / chunkSize + 1);
            int endY = Math.min(MAP_HEIGHT / chunkSize, (gp.viewport.y + gp.viewport.height) / TILE_SIZE / chunkSize + 1);

            for (int chunkX = startX; chunkX <= endX; chunkX++) {
                for (int chunkY = startY; chunkY <= endY; chunkY++) {
                    Point chunkPoint = new Point(chunkX, chunkY);
                    if (!generatedChunks.contains(chunkPoint)) {
                        spawnEnemiesInChunk(chunkX, chunkY, chunkSize);
                        generateGameObjectsInChunk(chunkX, chunkY, chunkSize);
                        generatedChunks.add(chunkPoint);
                    }
                }
            }
        }

        private void spawnEnemiesInChunk(int chunkX, int chunkY, int chunkSize) {
            double enemySpawnChance = 0.1;
            int maxEnemiesPerGroup = 3;

            int startX = chunkX * chunkSize;
            int startY = chunkY * chunkSize;
            int endX = Math.min(MAP_WIDTH, startX + chunkSize);
            int endY = Math.min(MAP_HEIGHT, startY + chunkSize);

            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    if (rand.nextDouble() < enemySpawnChance && isSuitableForEnemy(x, y)) {
                        int groupSize = rand.nextInt(maxEnemiesPerGroup) + 1;
                        for (int i = 0; i < groupSize; i++) {
                            int enemyX = x * TILE_SIZE + rand.nextInt(TILE_SIZE);
                            int enemyY = y * TILE_SIZE + rand.nextInt(TILE_SIZE);
                            System.out.println(enemyX + ", " + enemyY);
                            Rectangle enemyBounds = new Rectangle(enemyX, enemyY, 25, 25);
                            Enemy enemy = new Enemy(enemyBounds, gp);
                            gp.addEntity(enemy);
                        }
                    }
                }
            }
        }


        private void generateGameObjectsInChunk(int chunkX, int chunkY, int chunkSize) {
            int startX = chunkX * chunkSize;
            int startY = chunkY * chunkSize;
            int endX = Math.min(MAP_WIDTH, startX + chunkSize);
            int endY = Math.min(MAP_HEIGHT, startY + chunkSize);

            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    int worldX = x * TILE_SIZE;
                    int worldY = y * TILE_SIZE;

                    if (isSuitableForCloud(x, y) && rand.nextDouble() < 0.05) {
                        gameObjects.add(new Cloud(worldX, worldY, gp));
                    }

                    if (isSuitableForCopperVein(x, y) && rand.nextDouble() < 0.1) {
                        gameObjects.add(new CopperVein(worldX, worldY, gp));
                    }

                    if (isSuitableForAetherCrystal(x, y) && rand.nextDouble() < 0.05) {
                        gameObjects.add(new AetherCrystal(worldX, worldY, gp));
                    }

                    if (isSuitableForDecoration(x, y) && rand.nextDouble() < 0.1) {
                        gameObjects.add(new Decoration(worldX, worldY, gp));
                    }
                }
            }
        }

        public boolean isTileBuildable(int x, int y) {
            if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
                return false;
            }
            Tile tile = tiles[x][y];
            return tile.getProps().isBuildable();
        }

        public boolean isTileOccupied(int x, int y, Dimension buildingSize) {
            Rectangle buildingArea = new Rectangle(x * TILE_SIZE, y * TILE_SIZE,
                    buildingSize.width * TILE_SIZE,
                    buildingSize.height * TILE_SIZE);
            for (GameObject gameObject : gameObjects) {
                if (gameObject.getBounds().intersects(buildingArea)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isSuitableForCloud(int x, int y) {
            int minDistanceFromIsland = 2;
            int minDistanceFromObjects = 4;

            // check for min distance from island
            if (isWithinDistanceOfTileType(x, y, TileType.GRASS, minDistanceFromIsland)) {
                return false;
            }

            // check for min distance between clouds
            return isFarEnoughFromOtherObjects(x, y, minDistanceFromObjects);
        }

        private boolean isSuitableForCopperVein(int x, int y) {
            int minDistanceFromAir = 4;
            int minDistanceFromObjects = 6;

            // check for min distance air
            if (isWithinDistanceOfTileType(x, y, TileType.AIR, minDistanceFromAir)) {
                return false;
            }

            return isFarEnoughFromOtherObjects(x, y, minDistanceFromObjects);
        }

        private boolean isSuitableForAetherCrystal(int x, int y) {
            int minDistanceFromAir = 4;
            int minDistanceFromObjects = 4;

            if (isWithinDistanceOfTileType(x, y, TileType.AIR, minDistanceFromAir)) {
                return false;
            }

            return isFarEnoughFromOtherObjects(x, y, minDistanceFromObjects);
        }

        private boolean isSuitableForDecoration(int x, int y) {
            int minDistanceFromAir = 2;
            int minDistanceFromObjects = 2;

            if (isWithinDistanceOfTileType(x, y, TileType.AIR, minDistanceFromAir)) {
                return false;
            }

            return isFarEnoughFromOtherObjects(x, y, minDistanceFromObjects);
        }

        private boolean isFarEnoughFromOtherObjects(int x, int y, int minDistance) {
            for (GameObject gameObject : gameObjects) {
                Rectangle objectBounds = gameObject.getBounds().getBounds();
                if (Math.abs(objectBounds.x / TILE_SIZE - x) <= minDistance &&
                        Math.abs(objectBounds.y / TILE_SIZE - y) <= minDistance) {
                    return false;
                }
            }
            return true;
        }

        private boolean isWithinDistanceOfTileType(int x, int y, TileType tileType, int distance) {
            // checks that the tile type is not the same as the tile type being checked for
            if (tiles[x][y].getProps() == tileType) {
                return true;
            }

            for (int i = -distance; i <= distance; i++) {
                for (int j = -distance; j <= distance; j++) {
                    int checkX = x + i;
                    int checkY = y + j;
                    if (checkX >= 0 && checkX < MAP_WIDTH && checkY >= 0 && checkY < MAP_HEIGHT) {
                        if (tiles[checkX][checkY].getProps() == tileType) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private Point findBaseLocation() {
            int maxAttempts = 1000;
            for (int attempt = 0; attempt < maxAttempts; attempt++) {

                // random area to start check at
                int x = rand.nextInt(MAP_WIDTH - 10);
                int y = rand.nextInt(MAP_HEIGHT - 10);

                if (checkAreaForBase(x, y)) {
                    return new Point(x, y);
                }
            }
            gp.gameMap = new GameMap(MAP_WIDTH, MAP_HEIGHT, gp);

            // supposed to call recursively until a proper map has been found... (doesn't work)
            return null;
        }

        private boolean checkAreaForBase(int startX, int startY) {
            int minbaseSize = 25;
            int minDistanceFromAir = 5;

            // base area + distance from air
            for (int x = startX - minDistanceFromAir; x < startX + minbaseSize + minDistanceFromAir; x++) {
                for (int y = startY - minDistanceFromAir; y < startY + minbaseSize + minDistanceFromAir; y++) {
                    // coords are within boundaries
                    if (x >= 0 && y >= 0 && x < MAP_WIDTH && y < MAP_HEIGHT) {
                        // if any tile within base is air return false
                        if (tiles[x][y].getProps() == TileType.AIR) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        private Fog generateRandomFog(int x, int y) {
            double scale = 1.2 + rand.nextDouble() * 0.3;

            int fogWidth = (int)(fogTexture.getWidth() * scale);
            int fogHeight = (int)(fogTexture.getHeight() * scale);

            int fogX = x * TILE_SIZE - fogWidth / 4 + rand.nextInt(TILE_SIZE / 4) - TILE_SIZE / 8;
            int fogY = y * TILE_SIZE - fogHeight / 4 + rand.nextInt(TILE_SIZE / 4) - TILE_SIZE / 8;

            float alpha = 0.8f + rand.nextFloat() * 0.2f;

            return new Fog(fogX, fogY, fogWidth, fogHeight, alpha);

        }

        public void render(Graphics2D g2) {
            int startX = Math.max(0, gp.viewport.x / TILE_SIZE);
            int startY = Math.max(0, gp.viewport.y / TILE_SIZE);
            int endX = Math.min(MAP_WIDTH, (gp.viewport.x + gp.viewport.width) / TILE_SIZE + 1);
            int endY = Math.min(MAP_HEIGHT, (gp.viewport.y + gp.viewport.height) / TILE_SIZE + 1);

            renderTiles(g2, startX, startY, endX, endY);
            renderGameObjects(g2);
            renderFOW(g2, startX, startY, endX, endY);
            renderBaseCenterIdentifier(g2);
        }

        private void renderTiles(Graphics2D g2, int startX, int startY, int endX, int endY) {
            // render tiles within viewport
            if (tiles != null) {
                for (int x = startX; x < endX; x++) {
                    for (int y = startY; y < endY; y++) {
                        Tile tile = tiles[x][y];

                        if (shouldRenderTile(x, y)) {
                            if (tile.getProps() == TileType.GRASS) {
                                // determines if it is a corner piece, middle piece, etc...
                                TileType newType = determineType(x, y);
                                g2.drawImage(newType.getImage(), tile.getBounds().x, tile.getBounds().y, null);
                            } else {
                                g2.drawImage(tile.getProps().getImage(), tile.getBounds().x, tile.getBounds().y, null);
                            }
                        }
                    }
                }
            }
        }

        private void renderGameObjects(Graphics2D g2) {
            // render objects within viewport
            if (gameObjects != null) {
                for (GameObject gameObject : gameObjects) {
                    int tileX = gameObject.getBounds().x / TILE_SIZE;
                    int tileY = gameObject.getBounds().y / TILE_SIZE;
                    if (tiles[tileX][tileY].isExplored()) {
                        if (gp.viewport.intersects(gameObject.getBounds())) {
                            gameObject.render(g2);
                        }
                    }
                }
            }
        }

        private void renderFOW(Graphics2D g2, int startX, int startY, int endX, int endY) {
            // renders FOW
            if (tiles != null) {
                for (int x = startX; x < endX; x++) {
                    for (int y = startY; y < endY; y++) {
                        Tile tile = tiles[x][y];
                        if (!tile.isExplored()) {
                            Fog fogVar = tile.getFog();

                            Composite originalComposite = g2.getComposite();
                            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fogVar.alpha));
                            g2.drawImage(fogTexture, fogVar.fogX, fogVar.fogY, fogVar.fogWidth, fogVar.fogHeight, null);
                            g2.setComposite(originalComposite);
                        }
                    }
                }
            }
        }

        private void renderBaseCenterIdentifier(Graphics2D g2) {
            if (baseCenter != null) {
                int basePixelX = baseCenter.x * TILE_SIZE;
                int basePixelY = baseCenter.y * TILE_SIZE;

                int ovalWidth = 25;
                int ovalHeight = 25;

                g2.setColor(Color.BLUE);
                g2.drawOval(basePixelX - ovalWidth / 2, basePixelY - ovalHeight / 2, ovalWidth, ovalHeight);
                g2.setColor(Color.WHITE);
            }
        }

        public void renderMinimap(Graphics2D g2) {
            // minimap to viewport ratio
            double areaFactor = 2.0;

            // area (in tiles) to display on the minimap
            int displayWidth = (int) (GamePanel.SCREEN_WIDTH * areaFactor);
            int displayHeight = (int) (GamePanel.SCREEN_HEIGHT * areaFactor);

            // scale of minimap to display
            double scaleX = (double) MINIMAP_WIDTH / displayWidth;
            double scaleY = (double) MINIMAP_HEIGHT / displayHeight;

            // minimap position on gui
            int minimapX = MINIMAP_MARGIN;
            int minimapY = gp.getHeight() - MINIMAP_HEIGHT - MINIMAP_MARGIN;

            // offset of the area of the map to display
            int offsetX = Math.max(0, gp.camera.x - displayWidth / 4);
            int offsetY = Math.max(0, gp.camera.y - displayHeight / 4);

            // renders each tile on minimap
            for (int x = 0; x < MAP_WIDTH; x++) {
                for (int y = 0; y < MAP_HEIGHT; y++) {
                    int worldX = x * TILE_SIZE;
                    int worldY = y * TILE_SIZE;

                    // checks if tile is in the display area
                    if (worldX >= offsetX && worldX < offsetX + displayWidth &&
                            worldY >= offsetY && worldY < offsetY + displayHeight) {

                        int tileMinimapX = minimapX + (int) ((worldX - offsetX) * scaleX);
                        int tileMinimapY = minimapY + (int) ((worldY - offsetY) * scaleY);

                        // colors each tile according to its tile type

                        Color tileColor;
                        if (!tiles[x][y].isExplored()) {
                            tileColor = Color.DARK_GRAY;
                        } else if (tiles[x][y].getProps() == TileType.GRASS) {
                            tileColor = Color.GREEN.darker();
                        } else {
                            tileColor = Color.BLUE.darker();
                        }
                        g2.setColor(tileColor);
                        g2.fillRect(tileMinimapX, tileMinimapY, (int) (TILE_SIZE * scaleX), (int) (TILE_SIZE * scaleY));
                    }
                }
            }

            // renders map buildings
            for (Building building : getBuildings()) {
                // calculates buildings position and size on minimap
                int buildingMinimapX = minimapX + (int) ((building.getBounds().x - offsetX) * scaleX);
                int buildingMinimapY = minimapY + (int) ((building.getBounds().y - offsetY) * scaleY);
                int buildingMinimapWidth = (int) (building.getProps().getDimensions().width * TILE_SIZE * scaleX);
                int buildingMinimapHeight = (int) (building.getProps().getDimensions().height * TILE_SIZE * scaleY);

                // checks that building is in minimap bounds
                if (buildingMinimapX + buildingMinimapWidth <= minimapX + MINIMAP_WIDTH &&
                        buildingMinimapY + buildingMinimapHeight <= minimapY + MINIMAP_HEIGHT &&
                        buildingMinimapX >= minimapX && buildingMinimapY >= minimapY) {

                    // assigns a unique color for each building type
                    Color buildingColor = switch (building.getProps()) {
                        case TOWN_HALL -> new Color(50, 50, 200);
                        case BARRACKS -> new Color(150, 50, 200);
                        case STORAGE_HALL -> new Color(200, 50, 50);
                        case COPPER_MINE -> new Color(200, 150, 50);
                        case STEAM_GENERATOR -> new Color(200, 200 ,200);
                        case STEAM_LAB -> new Color(50, 200, 150);
                        default -> Color.WHITE;
                    };

                    // render building on the minimap
                    g2.setColor(buildingColor);
                    g2.fillRect(buildingMinimapX, buildingMinimapY, buildingMinimapWidth, buildingMinimapHeight);
                }
            }

            // renders camera viewport as a rect relative to the minimap
            int cameraMinimapX = minimapX + (int) ((gp.camera.x - offsetX) * scaleX);
            int cameraMinimapY = minimapY + (int) ((gp.camera.y - offsetY) * scaleY);
            int cameraMinimapWidth = (int) (GamePanel.SCREEN_WIDTH * scaleX);
            int cameraMinimapHeight = (int) (GamePanel.SCREEN_HEIGHT * scaleY);

            g2.setColor(Color.WHITE);
            g2.drawRect(cameraMinimapX, cameraMinimapY, cameraMinimapWidth, cameraMinimapHeight);
        }

        private boolean shouldRenderTile(int x, int y) {
            if (tiles[x][y].isExplored()) {
                return true;
            }

            int[][] directions = {
                    {-1, -1}, {0, -1}, {1, -1},
                    {-1, 0},           {1, 0},
                    {-1, 1},  {0, 1},  {1, 1}
            };

            for (int[] dir : directions) {
                int checkX = x + dir[0];
                int checkY = y + dir[1];

                if (checkX >= 0 && checkX < MAP_WIDTH && checkY >= 0 && checkY < MAP_HEIGHT) {
                    if (tiles[checkX][checkY].isExplored()) {
                        return true;
                    }
                }
            }

            return false;
        }

        public void exploreTilesAround (int centerX, int centerY, int radius) {
            int startX = Math.max(0, centerX - radius);
            int endX = Math.min(MAP_WIDTH, centerX + radius);
            int startY = Math.max(0, centerY - radius);
            int endY = Math.min(MAP_HEIGHT, centerY + radius);

            for (int x = startX; x <= endX; x++) {
                for (int y = startY; y <= endY; y++) {
                    if (Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= radius) {
                        tiles[x][y].setExplored(true);
                        System.out.println("EXPLORING");
                    }
                }
            }
        }

        private boolean isSuitableForEnemy(int x, int y) {
            int minDistanceFromOtherEnemies = 100;
            int viewportBuffer = TILE_SIZE * minDistanceFromOtherEnemies;
            Rectangle extendedViewport = new Rectangle(gp.viewport.x - viewportBuffer, gp.viewport.y - viewportBuffer,
                    gp.viewport.width + 2 * viewportBuffer, gp.viewport.height + 2 * viewportBuffer);

            for (Entity entity : gp.getEntities()) {
                if (entity instanceof Enemy && extendedViewport.intersects(entity.getBounds())) {
                    int enemyX = entity.getBounds().x / TILE_SIZE;
                    int enemyY = entity.getBounds().y / TILE_SIZE;
                    int distanceX = Math.abs(enemyX - x);
                    int distanceY = Math.abs(enemyY - y);
                    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

                    if (distance < minDistanceFromOtherEnemies) {
                        return false;
                    }
                }
            }
            return true;
        }



        public TileType determineType(int x, int y) {
            boolean airTop = isTileType(x, y - 1, TileType.AIR);
            boolean airBottom = isTileType(x, y + 1, TileType.AIR);
            boolean airLeft = isTileType(x - 1, y, TileType.AIR);
            boolean airRight = isTileType(x + 1, y, TileType.AIR);
            boolean airTopLeft = isTileType(x - 1, y - 1, TileType.AIR);
            boolean airTopRight = isTileType(x + 1, y - 1, TileType.AIR);
            boolean airBottomLeft = isTileType(x - 1, y + 1, TileType.AIR);
            boolean airBottomRight = isTileType(x + 1, y + 1, TileType.AIR);

            if (airTop && airLeft && airTopLeft) return TileType.GRASS_TOP_LEFT;
            if (airTop && airRight && airTopRight) return TileType.GRASS_TOP_RIGHT;
            if (airBottom && airLeft && airBottomLeft) return TileType.GRASS_BOTTOM_LEFT;
            if (airBottom && airRight && airBottomRight) return TileType.GRASS_BOTTOM_RIGHT;

            if (airLeft && !airTop && !airBottom) return TileType.GRASS_LEFT;
            if (airRight && !airTop && !airBottom) return TileType.GRASS_RIGHT;
            if (airTop && !airLeft && !airRight) return TileType.GRASS_TOP;
            if (airBottom && !airLeft && !airRight) return TileType.GRASS_BOTTOM;

            if (!airTop && !airLeft && airTopLeft) return TileType.GRASS_TOP_LEFT_CORNER;
            if (!airTop && !airRight && airTopRight) return TileType.GRASS_TOP_RIGHT_CORNER;
            if (!airBottom && !airLeft && airBottomLeft) return TileType.GRASS_BOTTOM_LEFT_CORNER;
            if (!airBottom && !airRight && airBottomRight) return TileType.GRASS_BOTTOM_RIGHT_CORNER;

            return TileType.GRASS;
        }

        private boolean isTileType(int x, int y, TileType type) {
            if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) return false;

            return tiles[x][y].getProps() == type;
        }

        // getters and setters

        public void update() {
            generateObjectsInViewPort();
        }

        public Tile getTile(int x, int y) {
            return tiles[x][y];
        }

        public void addGameObject(GameObject gameObject) {
            gameObjects.add(gameObject);
        }

        public boolean isWalkable(int x, int y) {
            if (x < 0 || x >= MAP_WIDTH || y < 0 || y >= MAP_HEIGHT) {
                return false;
            }

            boolean isTileWalkable = tiles[x][y].getProps() == TileType.GRASS;

            boolean isTileOccupied = isTileOccupied(x, y, new Dimension(1, 1));

            return isTileWalkable && !isTileOccupied;
        }

        public ArrayList<GameObject> getGameObjects() {
            return gameObjects;
        }

        public ArrayList<Building> getBuildings() {
            ArrayList<Building> buildings = new ArrayList<>();
            for (GameObject gameObject : gameObjects) {
                if (gameObject instanceof Building) {
                    buildings.add((Building) gameObject);
                }
            }
            return buildings;
        }

        public <T extends GameObject> ArrayList<T> getTypeOfGameObjects(Class<T> clazz) {
            ArrayList<T> typedGameObjects = new ArrayList<>();
            for (GameObject gameObject : gameObjects) {
                if (clazz.isInstance(gameObject)) {
                    typedGameObjects.add(clazz.cast(gameObject));
                }
            }
            return typedGameObjects;
        }

        public TownHall getTownHall() {
            return getTypeOfGameObjects(TownHall.class).get(0);
        }

        public Point getBaseCenter() {
            return baseCenter;
        }
    }

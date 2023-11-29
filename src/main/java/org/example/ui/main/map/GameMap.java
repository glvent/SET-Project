package org.example.ui.main.map;

import com.raylabz.opensimplex.OpenSimplexNoise;

import java.awt.*;
import java.util.Random;

public class Map {
    private Tile[][] tiles;
    private final int TILE_WIDTH = 32;
    private final int TILE_HEIGHT = 32;
    private final int width; // width in terms of number of tiles
    private final int height; // height in terms of number of tiles
    private OpenSimplexNoise noise;

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width][height];
        this.noise = new OpenSimplexNoise(new Random().nextLong());
        generateMap();
    }

    private void generateMap() {
        double noiseScale = 0.05;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double noiseValue = noise.getNoise2D(x * noiseScale, y * noiseScale).getValue();
                TileType type = (noiseValue > 0) ? TileType.ISLAND : TileType.AIR;
                tiles[x][y] = new Tile(type, x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return new Tile(TileType.AIR, x * TILE_WIDTH, y * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
        }
        return tiles[x][y];
    }

    public void render(Graphics g2) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile tile = tiles[x][y];
                g2.drawImage(tile.getType().getImage(), tile.getBounds().x, tile.getBounds().y, null);
            }
        }
    }

}

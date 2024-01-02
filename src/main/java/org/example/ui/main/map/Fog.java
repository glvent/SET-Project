package org.example.ui.main.map;

public class Fog {
    public int fogX;
    public int fogY;
    public int fogWidth;
    public int fogHeight;
    public float alpha;

    public Fog(int fogX, int fogY, int fogWidth, int fogHeight, float alpha) {
        this.fogX = fogX;
        this.fogY = fogY;
        this.fogWidth = fogWidth;
        this.fogHeight = fogHeight;
        this.alpha = alpha;
    }
}

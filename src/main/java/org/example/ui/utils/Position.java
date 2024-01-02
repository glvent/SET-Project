package org.example.ui.utils;

import java.awt.*;

public class Position {
    public int x, y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static double getDistance(Rectangle position, Rectangle position2) {
        double distance = position.x * position2.x + position.y * position2.y;
        return Math.sqrt(distance);
    }

    public static double getDistance(double dx, double dy, double dx2, double dy2) {
        double distance = dx * dx2 + dy * dy2;
        return Math.sqrt(distance);
    }
}

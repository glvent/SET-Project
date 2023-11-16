package org.example.ui.utils;

import java.awt.*;

public class DrawUtils {
    public static void drawCenteredString(Graphics g, String text, int fontSize, int rectX, int rectY, int rectWidth, int rectHeight) {
        // rect is most often just panel width & height
        Font originalFont = g.getFont();
        g.setFont(new Font(originalFont.getName(), originalFont.getStyle(), fontSize));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x = rectX + (rectWidth - metrics.stringWidth(text)) / 2;
        int y = rectY + ((rectHeight - metrics.getHeight()) / 2) + metrics.getAscent();
        g.drawString(text, x, y);
        // reset the font
        g.setFont(originalFont);
    }
}

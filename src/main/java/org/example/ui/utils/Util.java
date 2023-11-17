package org.example.ui.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Util {
    public static BufferedImage readImage(String path) throws IOException {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Objects.requireNonNull(Util.class.getResourceAsStream(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}

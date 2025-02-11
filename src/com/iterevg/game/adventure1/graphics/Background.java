package com.iterevg.game.adventure1.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class Background {
    Pixel[][] mask;
    byte[] bytes;
    int width;

    int height;

    public byte[] getBytes() {
        return bytes;
    }

    public Pixel[][] getMask() {
        return mask;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void read(String filePath, String maskPath) {
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            width = image.getWidth();
            height = image.getHeight();
            bytes = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();

            BufferedImage imageMask = ImageIO.read(new File(maskPath));
            mask = new Pixel[width][height];
            // Parcourir les pixels de l'image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Pixel p = new Pixel(imageMask.getRGB(x, y));
                    mask[x][y] = p;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
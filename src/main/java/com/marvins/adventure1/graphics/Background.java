package com.marvins.adventure1.graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class Background {
    byte[] bytes;
    Pixel[][] mask;
    Pixel[][] path;
    int width;
    int height;
    private boolean masking = true;

    public byte[] getBytes() {
        return bytes;
    }

    public Pixel[][] getMask() {
        return mask;
    }

    public Pixel[][] getPath() {
        return path;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void read(String filename) {
        String backimg = filename + ".png";
        String maskimg = filename + "_mask.png";
        String pathimg = filename + "_path.png";
        try {
            BufferedImage image = ImageIO.read((getClass().getClassLoader().getResourceAsStream("background/"+backimg)));
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.bytes = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
            this.mask = getPixels(maskimg);
            this.path = getPixels(pathimg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pixel[][] getPixels(String img) throws IOException {
        BufferedImage bi = ImageIO.read((getClass().getClassLoader().getResourceAsStream("background/"+img)));
        Pixel[][] pixels = new Pixel[this.width][this.height];
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                Pixel p = new Pixel(bi.getRGB(x, y));
                pixels[x][y] = p;
            }
        }
        return pixels;
    }

    public boolean isReachable(int x, int y) {
        return x>=0 && x<this.width && y>=0 && y< this.height && this.path[x][y].getRgbValue() == Color.WHITE.getRGB();
    }

    public void disableMasking() {
        this.masking = false;
    }

    public void enableMasking() {
        this.masking = true;
    }

    public boolean isMasking() {
        return this.masking;
    }
}
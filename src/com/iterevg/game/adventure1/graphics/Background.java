package com.iterevg.game.adventure1.graphics;

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

    public void read(String filename) {
        String backimg = filename + ".png";
        String maskimg = filename + "_mask.png";
        String pathimg = filename + "_path.png";
        try {
            BufferedImage image = ImageIO.read(new File(backimg));
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
        BufferedImage bi = ImageIO.read(new File(img));
        Pixel[][] pixels = new Pixel[this.width][this.height];
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                Pixel p = new Pixel(bi.getRGB(x, y));
                pixels[x][y] = p;
            }
        }
        return pixels;
    }

    public boolean isReachable(int mouseX, int mouseY) {
        return mouseX>=0 && mouseX<this.width && mouseY>=0 && mouseY< this.height && this.path[mouseX][mouseY].getRgbValue() == Color.WHITE.getRGB();
    }
}
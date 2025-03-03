package com.marvins.adventure1.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.marvins.adventure1.Constants.SPR_HEIGHT;
import static com.marvins.adventure1.Constants.SPR_WIDTH;

public class Sprite {
    Pixel[][] pixels;
    private int width;
    private int height;
    private int frame = 0;
    private boolean isWalking = false;

    public Pixel[][] getPixels() {
        return pixels;
    }

    public void setPixels(Pixel[][] pixels) {
        this.pixels = pixels;
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

    public int getFrame() {
        return frame;
    }

    public void incFrame() {
        frame++;
        frame = frame % 10 + 1;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalk() {
        isWalking = true;
    }

    public void setNoWalk() {
        isWalking = false;
        frame = 0;
    }

    public void getSpriteFromFile(String spr, int i) {
        try {
            BufferedImage image = ImageIO.read((getClass().getClassLoader().getResourceAsStream("sprites/spr_"+spr+".png")));
            this.width = SPR_WIDTH;
            this.height = SPR_HEIGHT;
            int width = image.getWidth();
            int height = image.getHeight();
            pixels = new Pixel[width][height];
            // Parcourir les pixels de l'image
            for (int y = 0; y < SPR_HEIGHT; y++) {
                for (int x = 0; x < width; x++) {
                    try {
                        Pixel p = new Pixel(image.getRGB(x, y + (i * SPR_HEIGHT)));
                        pixels[x][y] = p;
                    } catch (Exception e) {
                        System.err.println("");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSpriteFromFile(String filePath) {
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            int width = image.getWidth();
            int height = image.getHeight();
            pixels = new Pixel[width][height];
            // Parcourir les pixels de l'image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Pixel p = new Pixel(image.getRGB(x, y));
                    pixels[x][y] = p;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

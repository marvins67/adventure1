package com.iterevg.game.adventure1.graphics;

import com.iterevg.game.adventure1.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

public class Image {
    Pixel[] pixels;

    byte[] bytes;

    int width;

    int height;

    public Pixel[] getPixels() {
        return pixels;
    }

    public void setPixels(Pixel[] pixels) {
        this.pixels = pixels;
    }

    public byte[] getBytes() {
        return bytes;
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

    public void read(String filePath) {
        try {
            // Charger l'image BMP
            BufferedImage image = ImageIO.read(new File(filePath));
            if(Constants.GAME_SCALE > 1) {
                image = resizeImage(image);
            }
            width = image.getWidth();
            height = image.getHeight();
            pixels = new Pixel[width * height];
            // Parcourir les pixels de l'image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Pixel p = new Pixel(image.getRGB(x, y));
                    pixels[x + (width *y)] = p;
                }
            }
            bytes = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage resizeImage(BufferedImage originalImage) {
        int newWidth = originalImage.getWidth() * Constants.GAME_SCALE;
        int newHeight = originalImage.getHeight() * Constants.GAME_SCALE;
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g2d.dispose(); // Libère les ressources graphiques

        return resizedImage;
    }
}
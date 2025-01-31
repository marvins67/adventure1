package com.iterevg.game.adventure1.graphics;

import com.iterevg.game.adventure1.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;

import static com.iterevg.game.adventure1.Constants.*;

public class ScreenRenderer extends JPanel {

    private int fps = 0;
    private final static BufferedImage bufferedImage = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void drawSprite(Sprite sprite) {
        drawSprite(sprite.getImage(), sprite.getX(), sprite.getY(), sprite.getFrame());
    }

    private void drawSprite(Image image, int ox, int oy, int frame) {
        for (int i = 0; i < Constants.SPR_SIZE; i++) {
            // MAGENTA = Transparence
            int q = i / SPR_WIDTH;
            int r = i % SPR_WIDTH;
            int rp = (image.getWidth() * q) + r + (frame * SPR_WIDTH);
            Pixel p = image.getPixels()[rp];
            if (p.getRgbValue() != Color.MAGENTA.getRGB()) {
                int nx = (p.getX() % SPR_WIDTH) + ox;
                int ny = p.getY() + oy;
                if (nx >= 0 && nx < GAME_WIDTH && ny >= 0 && ny < GAME_HEIGHT) {
                    bufferedImage.setRGB(nx, ny, p.getRgbValue());
                }
            }
        }
    }

    public void drawBackground(byte[] frame) {
        byte[] imgData = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        Arrays.fill(imgData, (byte) 0); //Remplit l'écran de noir
        System.arraycopy(frame,0,imgData,0,frame.length);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, this);

        // Dessiner les FPS
        g.setColor(Color.WHITE); // Couleur du texte (blanc)
        g.drawString("FPS: " + fps, 10, 20); // Afficher les FPS en haut à gauche
    }
}

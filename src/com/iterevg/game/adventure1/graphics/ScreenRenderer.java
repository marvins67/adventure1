package com.iterevg.game.adventure1.graphics;

import com.iterevg.game.adventure1.Actor;
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

    public void drawActor(Actor actor) {
        for (int i = 0; i < Constants.SPR_SIZE; i++) {
            Pixel p = getPixelFromRank(actor.getSprite(), i);
            if (!pixelTransparent(p)) {
                //int nx = (p.getPosition().getX() % actor.getSpriteWR().getWidth()) + actor.getPosition().getX();
                //int ny = p.getPosition().getY() + actor.getPosition().getY();
                int nx = i % actor.getSprite().getWidth() + actor.getPosition().getX();
                int ny = i / actor.getSprite().getHeight() + actor.getPosition().getY();
                if (positionInScreen(nx, ny)) {
                    bufferedImage.setRGB(nx, ny, p.getRgbValue());
                }
            }
        }
    }

    private static boolean positionInScreen(int nx, int ny) {
        return nx >= 0 && nx < GAME_WIDTH && ny >= 0 && ny < GAME_HEIGHT;
    }

    private static boolean pixelTransparent(Pixel p) {
        return p.getRgbValue() == Color.MAGENTA.getRGB();
    }

    private Pixel getPixelFromRank(Sprite sprite, int i) {
        int q = i / sprite.getWidth();
        int r = i % sprite.getWidth();
        int rp = (sprite.getImage().getWidth() * q) + r + (sprite.getFrame() * sprite.getWidth());
        return sprite.getImage().getPixels()[rp];
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

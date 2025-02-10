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
    private final static BufferedImage bufferedImage = new BufferedImage(SCR_WIDTH, SCR_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

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
                int ny = i / actor.getSprite().getWidth() + actor.getPosition().getY();
                if (positionInScreen(nx, ny)) {
                    bufferedImage.setRGB(nx, ny, p.getRgbValue());
                }
            }
        }
    }

    public void drawSprite(Sprite sprite) {
        for (int j = 0; j < sprite.getHeight(); j++) {
            for (int i = 0; i < sprite.getWidth(); i++) {
                try {
                    if (positionInScreen(i, j)) {
                        bufferedImage.setRGB(i, j, sprite.getPixels()[i][j].getRgbValue());
                    }
                } catch (Exception e) {
                    System.out.print("Erreur");
                }
            }
        }
    }
    private static boolean positionInScreen(int nx, int ny) {
        return nx >= 0 && nx < SCR_WIDTH && ny >= 0 && ny < SCR_HEIGHT;
    }

    private static boolean pixelTransparent(Pixel p) {
        return p.getRgbValue() == Color.MAGENTA.getRGB();
    }

    private Pixel getPixelFromRank(Sprite sprite, int i) {
        int q = i / sprite.getWidth();
        int r = i % sprite.getWidth();
        return sprite.getPixels()[r][q];
    }

    public void drawBackground(byte[] frame) {
        byte[] imgData = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        Arrays.fill(imgData, (byte) 0); //Remplit l'écran de noir
        System.arraycopy(frame,0,imgData,0,frame.length);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage bf;
        if(Constants.GAME_SCALE > 1) {
            bf = resizeImage(bufferedImage);
        } else {
            bf = bufferedImage;
        }

        g.drawImage(bf, 0, 0, this);

        // Dessiner les FPS
        g.setColor(Color.WHITE); // Couleur du texte (blanc)
        g.drawString("FPS: " + fps, 10, 20); // Afficher les FPS en haut à gauche
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

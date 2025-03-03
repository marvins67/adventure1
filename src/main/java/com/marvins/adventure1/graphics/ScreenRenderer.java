package com.marvins.adventure1.graphics;

import com.marvins.adventure1.Actor;
import com.marvins.adventure1.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.marvins.adventure1.Constants.*;

public class ScreenRenderer extends JPanel {
    private static final BufferedImage bufferedImage = new BufferedImage(SCR_WIDTH, SCR_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
    private Map<String, Cross> crosses = new HashMap<>();

    public void drawActor(Actor actor, Pixel[][] mask, boolean masking) {
        for (int i = 0; i < Constants.SPR_SIZE; i++) {
            Pixel p = getPixelFromRank(actor.getSprite(), i);
            if (!pixelTransparent(p)) {
                int nx = i % actor.getSprite().getWidth() + actor.getPosition().getX();
                int ny = i / actor.getSprite().getWidth() + actor.getPosition().getY();
                if (positionInScreen(nx, ny) && (!masking || !pixelHidden(mask[nx][ny]))) {
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
        return nx >= 0 && nx < SCR_WIDTH && ny >= 0 && ny < 124;
    }

    private boolean pixelHidden(Pixel p) {
        return p.getRgbValue() == Color.BLACK.getRGB();
    }

    private static boolean pixelTransparent(Pixel p) {
        return p.getRgbValue() == Color.MAGENTA.getRGB();
    }

    private Pixel getPixelFromRank(Sprite sprite, int i) {
        int q = i / sprite.getWidth();
        int r = i % sprite.getWidth() + (sprite.getFrame()*sprite.getWidth());
        return sprite.getPixels()[r][q];
    }

    public void drawBackground(byte[] frame) {
        byte[] imgData = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        Arrays.fill(imgData, (byte) 0); //Remplit l'écran de noir
        System.arraycopy(frame,0,imgData,0,frame.length);
    }

    public void addCross(String name, int x, int y) {
        Cross c = new Cross();
        c.x =x; c.y = y;
        if (crosses.containsKey(name)) {
            crosses.replace(name, c);
        } else {
            crosses.put(name, c);
        }
    }

    private void drawCross(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.RED);
        g2d.drawLine(x, y, x + 4, y + 4);
        g2d.drawLine(x + 4, y, x, y + 4);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = bufferedImage.createGraphics();
        for(Cross c : crosses.values()) {
            drawCross(g2d, c.x, c.y);
        }

        BufferedImage bf;
        if(Constants.GAME_SCALE > 1) {
            bf = resizeImage(bufferedImage);
        } else {
            bf = bufferedImage;
        }
        g.drawImage(bf, 0, 0, this);
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

    private class Cross {
        int x; int y;
    }
}
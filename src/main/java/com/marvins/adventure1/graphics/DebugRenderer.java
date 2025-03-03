package com.marvins.adventure1.graphics;

import com.marvins.adventure1.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.marvins.adventure1.Constants.*;

public class DebugRenderer extends JPanel {

    private int fps = 0;
    private List<String> lines = new ArrayList<>();
    private static final BufferedImage bufferedImage = new BufferedImage(SCR_WIDTH, SCR_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void addAction(String action) {
        this.lines.add(action);
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
        g.drawString("Last action: ", 10, 35);
        int h = 50;
        for (int i = lines.size()-1; i>=0 ; i--) {
            g.drawString(lines.get(i), 10, h);
            h = h + 15;
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
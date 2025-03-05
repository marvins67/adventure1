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
    private static final BufferedImage bufferedImage = new BufferedImage(SCR_WIDTH, 1024, BufferedImage.TYPE_3BYTE_BGR);

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void addAction(String action) {
        this.lines.add(action);
        System.err.println(action);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufferedImage, 0, 0, this);

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
}
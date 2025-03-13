package com.marvins.adventure1.graphics;

import com.marvins.adventure1.Constants;
import com.marvins.adventure1.walkbox.WbFile;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.marvins.adventure1.Constants.SCR_HEIGHT;
import static com.marvins.adventure1.Constants.SCR_WIDTH;

public class WalkBoxSidebarRenderer extends JPanel {

    private int fps = 0;
    private List<WbFile> filenames = new ArrayList<>();
    private static final BufferedImage bufferedImage = new BufferedImage(SCR_WIDTH, SCR_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void addFilename(WbFile wbf) {
        this.filenames.add(wbf);
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

        int h = 20;
        for (int i=0 ; i < filenames.size(); i++) {
            g.setColor(filenames.get(i).isSelected() ? Color.RED : Color.WHITE);
            g.drawString(filenames.get(i).getFile(), 10, h);
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
package com.marvins.adventure1.graphics;

import com.marvins.adventure1.Actor;
import com.marvins.adventure1.Constants;
import com.marvins.adventure1.SignEnum;
import com.marvins.adventure1.tool.Polygon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.*;
import java.util.List;

import static com.marvins.adventure1.Constants.*;

public class ScreenRenderer extends JPanel {
    private static final BufferedImage bufferedImage = new BufferedImage(SCR_WIDTH, SCR_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
    private Map<String, Sign> signes = new HashMap<>();

    public void drawActor(Actor actor, Pixel[][] mask, boolean masking) {
        for (int i = 0; i < Constants.SPR_SIZE; i++) {
            Pixel p = getPixelFromRank(actor.getSprite(), i);
            if (!pixelTransparent(p)) {
                int nx = i % actor.getSprite().getWidth() + (int)actor.getPosition().getX();
                int ny = i / actor.getSprite().getWidth() + (int)actor.getPosition().getY();
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

    public void addSign(String name, int x, int y, SignEnum sign) {
        Sign c = new Sign();
        c.x =x; c.y = y; ; c.sign = sign;
        if (signes.containsKey(name)) {
            signes.replace(name, c);
        } else {
            signes.put(name, c);
        }
    }

    public void freeSignes(String path) {
        List<String> kp = new ArrayList<>();
        for (String k : signes.keySet()) {
            if (k.length() >= path.length() && k.startsWith(path)) {
                kp.add(k);
            }
        }
        for (String s : kp) {
            signes.remove(s);
        }
    }

    private void drawCross(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.RED);
        g2d.drawLine(x, y, x + 4, y + 4);
        g2d.drawLine(x + 4, y, x, y + 4);
    }

    private void drawPoint(Graphics2D g2d, int x, int y) {
        g2d.setColor(Color.BLUE);
        g2d.drawLine(x, y, x, y);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = bufferedImage.createGraphics();
        for(Sign c : signes.values()) {
            if(c.sign == SignEnum.CROSS) {
                drawCross(g2d, c.x, c.y);
            } else if (c.sign == SignEnum.POINT) {
                drawPoint(g2d, c.x, c.y);
            }
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

    public void drawWalkBoxes(Polygon polygon) {
        drawWalkBoxes(polygon, Color.BLUE);
    }

    public void drawWalkBoxes(Polygon polygon,Color c) {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(c);
        if (!polygon.getPositions().isEmpty()) {
            if (polygon.getPositions().size() == 1){
                Point p = polygon.getPositions().get(0);
                g2d.drawLine(p.x, p.y, p.x, p.y);
            }
            for(int i=0 ; i< polygon.getPositions().size()-1 ; i++) {
                Point p1 = polygon.getPositions().get(i);
                Point p2 = polygon.getPositions().get(i+1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            if (polygon.getPositions().size() > 2){
                Point p1 = polygon.getPositions().get(polygon.getPositions().size()-1);
                Point p2 = polygon.getPositions().get(0);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    public void drawWalkMap(WalkMap wm) {
        for(Polygon p : wm.getPolygons()) {
            drawWalkBoxes(p, Color.GREEN);
        }
    }

    public void addWalkBoxKeys() {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.drawString("Left clic : Add point", 5, 135);
        g2d.drawString("Right clic : Remove polygon", 5, 150);
        g2d.drawString("A : Add polygon", 160, 135);
        g2d.drawString("R : Remove last polygon ", 160, 150);
        g2d.drawString("S : Save polygon ", 160, 165);
    }
    private static class Sign {
        int x; int y;
        SignEnum sign;
    }
}
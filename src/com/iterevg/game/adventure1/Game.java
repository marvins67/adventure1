package com.iterevg.game.adventure1;

import com.iterevg.game.adventure1.graphics.*;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.iterevg.game.adventure1.Constants.*;

public class Game extends MouseAdapter {

    private JFrame frame;
    private ScreenRenderer screenRenderer;
    private Image background;
    private Actor actor;
    private volatile boolean running = true;

    int mouseX = 145 * GAME_SCALE;
    int mouseY = 36 * GAME_SCALE;
    int destX = 0;
    int destY = 0;
    boolean mouseClicked = false;
    boolean doWalk = false;
    boolean moveX = false;
    boolean moveY = false;

   public void init(){
       screenRenderer = new ScreenRenderer();
       screenRenderer.addMouseListener(this);

       frame = new JFrame(Constants.NAME);
       frame.add(screenRenderer);
       frame.setSize(GAME_WIDTH, GAME_HEIGHT);
       frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
       frame.setResizable(false);
       frame.setVisible(true);
       frame.addKeyListener(new KeyAdapter() {
           @Override
           public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                   running = false;
               }
           }
       });

       background = new Image();
       background.read("gk_back.png");

       actor = new Actor();
       actor.setPosition(new Position(mouseX, mouseY));

       Image image = new Image();
       image.read("spr_gk1.png");
       Sprite sprite = new Sprite();
       sprite.setImage(image);
       sprite.setWidth(SPR_WIDTH);
       sprite.setHeight(SPR_HEIGHT);

       actor.sprites[0] = sprite;
       actor.sprites[2] = invertSprite(sprite);
   }

    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0 / Constants.FRAMERATE; // Temps par tick pour n FPS
        double deltaTime = 0;
        double frameRateLimit = 0;
        int frames = 0;

        while (running) {
            long now = System.nanoTime(); // Récupérer le temps actuel
            deltaTime += (now - lastTime); // Calculer le delta
            frameRateLimit += (now - lastTime) / nsPerTick;
            lastTime = now;

            //On ne met à jour l'affichage que n fois par secondes (Constants.FRAMERATE)
            while (frameRateLimit >= 1) {
                frameRateLimit--;
                screenRenderer.drawBackground(background.getBytes());
                screenRenderer.drawActor(actor);
                screenRenderer.repaint();
                updateScreen();
            }

            frames++;

            // Affiche les FPS toutes les secondes
            if (deltaTime >= Constants.ONE_SECOND ){
                deltaTime = 0;
                screenRenderer.setFps(frames);
                frames = 0;
            }
        }
    }

    private void updateScreen() {
       //Mise à jour des données du jeu
        int v = 2 * GAME_SCALE;
        if (mouseClicked) {
            destX = mouseX;
            destY = mouseY;
            mouseClicked = false;
            moveX = Math.abs(footX() - destX) >= v;
            moveY = Math.abs(footY() - destY) >= v;
            if (moveX || moveY) {
                doWalk = true;
            }
        }

        if (doWalk) {
            moveX = Math.abs(footX() - destX) >= v;
            moveY = Math.abs(footY() - destY) >= v;
            System.out.println("click : " + destX + ", " + destY + " sprite : " + footX() + ", " + footY());
            actor.getSprite().incFrame();
            actor.getSprite().setWalk();
            if (moveX) {
                int dx = v * (footX() < destX ? 1 : -1);
                actor.setDirection(dx > 0 ? 0 : 2);
                actor.getPosition().setX(actor.getPosition().getX() + dx);
            }
            if (moveY) {
                int sens = (footY() < destY ? 1 : -1);
                int dy = v * sens;
                actor.getPosition().setY(actor.getPosition().getY() + dy);
            }
            if (!moveX && !moveY) {
                doWalk = false;
                actor.getSprite().setNoWalk();
            }
        }
    }

    //TODO : On recalcule trop de fois footX et footY
    private int footX() {
        return actor.getPosition().getX() + (actor.getSprite().getWidth() / 2);
    }

    private int footY() {
        return actor.getPosition().getY() + actor.getSprite().getHeight() - 4;
    }


    private Sprite invertSprite(Sprite sprite) {
        Sprite invertedSprite = new Sprite();
        invertedSprite.setWidth(sprite.getWidth());
        invertedSprite.setHeight(sprite.getHeight());

        int size = sprite.getWidth();
        int length = sprite.getImage().getPixels().length;
        Image invertedImage = new Image();
        Pixel[] invertedPixel = new Pixel[length];
        for (int i = 0; i < length; i++) {
            int newPosition = (i / size) * size + (size - 1 - (i % size));
            Pixel p = new Pixel(sprite.getImage().getPixels()[i].getRgbValue());
            invertedPixel[newPosition] = p;
        }
        invertedImage.setHeight(sprite.getImage().getHeight());
        invertedImage.setWidth((sprite.getImage().getWidth()));
        invertedImage.setPixels(invertedPixel);
        invertedSprite.setImage(invertedImage);
        return invertedSprite;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseClicked = true;
    }

    public void stop() {
       frame.dispose();
       System.out.println("That's all folks!");
    }
}

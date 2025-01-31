package com.iterevg.game.adventure1;

import com.iterevg.game.adventure1.graphics.Image;
import com.iterevg.game.adventure1.graphics.ScreenRenderer;
import com.iterevg.game.adventure1.graphics.Sprite;

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
    private Sprite sprite;
    private volatile boolean running = true;

    int mouseX = 580;
    int mouseY = 147;

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

       Image image = new Image();
       image.read("spr_gk1.png");
       sprite = new Sprite();
       sprite.setImage(image);
       sprite.setX(580);
       sprite.setY(147);
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
                screenRenderer.drawSprite(sprite);
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
        int v = 2 * SCALE;
        boolean moveX = Math.abs(sprite.getX() - mouseX) >= v;
        boolean moveY = Math.abs(sprite.getY() - mouseY) >= v;
        if (moveX || moveY) {
            System.out.println("click : " + mouseX + ", " + mouseY + " sprite : " + sprite.getX() + ", " + sprite.getY());
            sprite.incFrame();
            sprite.setWalk();
            if (moveX) {
                int dx = v * (sprite.getX() < mouseX ? 1 : -1);
                sprite.setX(sprite.getX() + dx);
            }
            if (moveY) {
                int dy = v * (sprite.getY() < mouseY ? 1 : -1);
                sprite.setY(sprite.getY() + dy);
            }
        } else {
            sprite.setNoWalk();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void stop(){
       frame.dispose();
       System.out.println("That's all folks!");
    }
}

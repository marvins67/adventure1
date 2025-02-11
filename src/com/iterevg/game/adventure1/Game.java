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
    private Background background;
    private Actor actor;
    private volatile boolean running = true;

    int mouseX = 145;
    int mouseY = 36;
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

       background = new Background();
       background.read("gk_back.png", "gk_back_mask.png");

       actor = new Actor();
       actor.addSprite("spr_gk0.png", 0);
       actor.addSprite("spr_gk1.png", 1);
       actor.addSprite("spr_gk2.png", 2);
       actor.addSprite("spr_gk3.png", 3);
       actor.setPosition(new Position(mouseX, mouseY));
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
                screenRenderer.drawActor(actor, background.getMask());
                //screenRenderer.drawSprite(actor.getSprites()[3]);
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
        int v = 3;
        if (mouseClicked) {
            destX = mouseX / GAME_SCALE;
            destY = mouseY / GAME_SCALE;
            mouseClicked = false;
            moveX = Math.abs(footX() - destX) >= v;
            moveY = Math.abs(footY() - destY) >= v;
            if (moveX || moveY) {
                doWalk = true;
            }
        }

        if (doWalk) {
            moveX = Math.abs(footX() - destX) >= v && actor.getPosition().getX() > 0 && actor.getPosition().getX() < background.getWidth();
            moveY = Math.abs(footY() - destY) >= v && actor.getPosition().getY() > 0 && actor.getPosition().getY() < background.getHeight();
            //System.out.println("click : " + destX + ", " + destY + " sprite : " + footX() + ", " + footY());
            actor.getSprite().incFrame();
            actor.getSprite().setWalk();
            if (moveY) {
                int dy = v * (footY() < destY ? 1 : -1);
                actor.setDirection(dy > 0 ? 1 : 3);
                actor.getPosition().setY(actor.getPosition().getY() + dy);
            }
            if (moveX) {
                int dx = v * (footX() < destX ? 1 : -1);
                actor.setDirection(dx > 0 ? 0 : 2);
                actor.getPosition().setX(actor.getPosition().getX() + dx);
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
        return actor.getPosition().getY() + actor.getSprite().getHeight() - 1;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseClicked = true;
    }

    public void stop() {
       frame.dispose();
       System.out.println("That's all folks!");
    }
}

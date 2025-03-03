package com.marvins.adventure1;

import com.marvins.adventure1.graphics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.marvins.adventure1.Constants.*;

public class Game extends MouseAdapter {

    private JFrame frame;
    private ScreenRenderer screenRenderer;

    private DebugRenderer debugRenderer;
    private Background background;
    private Actor actGk;
    private volatile boolean running = true;
    private int mouseX = 145;
    private int mouseY = 36;
    private int destX = 0;
    private int destY = 0;
    private boolean mouseClicked = false;
    private boolean doWalk = false;

    public void init(){
        JFrame debug;
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

        debugRenderer = new DebugRenderer();
        debug = new JFrame("Debug");
        debug.setLocation(800, 0);
        debug.add(debugRenderer);
        debug.setSize(250, 800);
        debug.setBackground(Color.BLACK);
        debug.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        debug.setResizable(false);
        debug.setVisible(true);

        background = new Background();
        background.read("gk_back");
        background.disableMasking();

        actGk = new Actor();
        actGk.addSprites("gk");
        actGk.setPosition(new Position(mouseX, mouseY));
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
                screenRenderer.drawActor(actGk, background.getMask(), background.isMasking());
                screenRenderer.repaint();
                debugRenderer.repaint();
                updateEngine();
            }

            frames++;

            // Affiche les FPS toutes les secondes
            if (deltaTime >= Constants.ONE_SECOND ){
                deltaTime = 0;
                debugRenderer.setFps(frames);
                frames = 0;
            }
        }
    }

    private void updateEngine() {
        boolean moveY;
        boolean moveX;
        //Mise à jour des données du jeu
        int v = 3;
        int footX = footX();
        int footY = footY();
        if (mouseClicked) {
            destX = mouseX / GAME_SCALE;
            destY = mouseY / GAME_SCALE;

            screenRenderer.addCross("clic", destX, destY);

            boolean isReachable = background.isReachable(destX, destY);
            debugRenderer.addAction("Clic : " + destX + " ; " + destY + " reachable = " + isReachable);
            mouseClicked = false;
            moveX = Math.abs(footX - destX) >= v;
            moveY = Math.abs(footY - destY) >= v;
            if (moveX || moveY) {
                doWalk = true;
            }
        }

        if (doWalk) {

            moveX = Math.abs(footX - destX) >= v && actGk.getPosition().getX() >= 0 && actGk.getPosition().getX() < background.getWidth();
            moveY = Math.abs(footY - destY) >= v && actGk.getPosition().getY() >= (1 - actGk.getSprite().getHeight()) && actGk.getPosition().getY() < background.getHeight();
            actGk.getSprite().incFrame();
            actGk.getSprite().setWalk();

            //Calcule droite entre départ et arrivée:
            // y = ax + b
            // x = (y - b) /a
            double a = (double) ((-1)*destY - (-1)*footY) / (destX - footX);
            double b = (-1)*footY - (a * footX) + 34;
            debugRenderer.addAction("a="+a+" b="+b);
            if (moveX || moveY) {
                if (Math.abs(footX - destX) > Math.abs(footY - destY)) {
                    int dx = v * (footX < destX ? 1 : -1);
                    actGk.setDirection(dx > 0 ? 0 : 2);
                    actGk.getPosition().setX(actGk.getPosition().getX() + dx);
                    actGk.getPosition().setY((int) Math.abs((a * actGk.getPosition().getX()) + b));
                } else {
                    int dy = v * (footY < destY ? 1 : -1);
                    actGk.setDirection(dy > 0 ? 1 : 3);
                    actGk.getPosition().setY(actGk.getPosition().getY() + dy);
                    actGk.getPosition().setX((int) (((-1) * actGk.getPosition().getY() - b) / a));
                }
            }

            /*
            if (moveY) {
                int dy = v * (footY < destY ? 1 : -1);
                actGk.setDirection(dy > 0 ? 1 : 3);
                if (background.isReachable(footX, footY+ dy)) {
                    actGk.getPosition().setY(actGk.getPosition().getY() + dy);
                }
            }
            if (moveX) {
                int dx = v * (footX < destX ? 1 : -1);
                actGk.setDirection(dx > 0 ? 0 : 2);

                if (background.isReachable(footX + dx, footY)) {
                    actGk.getPosition().setX(actGk.getPosition().getX() + dx);
                }
            }

             */
            if (!moveX && !moveY) {
                doWalk = false;
                actGk.getSprite().setNoWalk();
            }
        }
    }

    private int footX() {
        return actGk.getPosition().getX() + (actGk.getSprite().getWidth() / 2);
    }

    private int footY() {
        return actGk.getPosition().getY() + actGk.getSprite().getHeight() - 1;
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
package com.marvins.adventure1;

import com.marvins.adventure1.graphics.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static com.marvins.adventure1.Constants.*;

public class Game extends MouseAdapter {

    private JFrame frame;
    private JFrame debug;
    private ScreenRenderer screenRenderer;
    private DebugRenderer debugRenderer;
    private Background background;
    private Actor actGk;
    private volatile boolean running = true;
    private volatile boolean showDebug = false;
    private int mouseX = 145;
    private int mouseY = 36;
    private int destX = 0;
    private int destY = 0;
    private boolean mouseClicked = false;
    private boolean doWalk = false;

    public void init(){
        screenRenderer = new ScreenRenderer();
        screenRenderer.addMouseListener(this);

        frame = new JFrame(Constants.NAME);
        frame.add(screenRenderer);
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(e -> {
                    if (KeyEvent.KEY_PRESSED == e.getID()) {
                        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                            running = false;
                        } else if (e.getKeyCode() == KeyEvent.VK_D) {
                            showDebug = !showDebug;
                        }
                    }
                    return false;
                });

        debugRenderer = new DebugRenderer();
        debug = new JFrame("Debug");
        debug.setLocation(1024, 0);
        debug.add(debugRenderer);
        debug.setSize(250, 1024);
        debug.setBackground(Color.BLACK);
        debug.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        debug.setResizable(true);
        debug.setVisible(false);

        background = new Background();
        background.read("gk_back");
        //background.disableMasking();

        actGk = new Actor();
        actGk.addSprites("gk");
        actGk.setPosition(new Point(mouseX, mouseY));
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
            debug.setVisible(showDebug);

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

        if (mouseClicked) {
            destX = mouseX / GAME_SCALE;
            destY = mouseY / GAME_SCALE;

            screenRenderer.addSign("clic", destX, destY, SignEnum.CROSS);

            boolean isReachable = background.isReachable(destX, destY);
            debugRenderer.addAction("Actor : " + actGk.getPosition().getX() + " ; " + actGk.getPosition().getY());
            debugRenderer.addAction("Foot : " + actGk.getFoot().getX() + " ; " + actGk.getFoot().getY());
            debugRenderer.addAction("Clic : " + destX + " ; " + destY + " reachable = " + isReachable);
            mouseClicked = false;
            moveX = Math.abs(actGk.getFoot().getX() - destX) >= v;
            moveY = Math.abs(actGk.getFoot().getY() - destY) >= v;
            if (isReachable && (moveX || moveY)) {
                doWalk = true;
            }
        }

        if (doWalk) {
            //debugRenderer.addAction("Actor : " + actGk.getPosition().getX() + " ; " + actGk.getPosition().getY());
            //debugRenderer.addAction("Foot : " + footX + " ; " + footY);

            moveX = Math.abs(actGk.getFoot().getX() - destX) >= v;
            moveY = Math.abs(actGk.getFoot().getY() - destY) >= v;

            if (moveX || moveY) {
                List<Pathfinder.Node> path = Pathfinder.findPath(background.getWalkable(), actGk.getFoot().x, actGk.getFoot().y, destX, destY);
                screenRenderer.freeSignes("path");
                for(int i=0 ; i < path.size() ; i++) {
                    Pathfinder.Node n = path.get(i);
                    screenRenderer.addSign("path"+i, n.x, n.y, SignEnum.POINT);
                }
                moveActorPathFinder(path, v, actGk.getFoot().x, actGk.getFoot().y);
            } else  {
                doWalk = false;
                actGk.getSprite().setNoWalk();
            }
        }
    }

    private void moveActorPathFinder(List<Pathfinder.Node> path, int v, int footX, int footY) {
        int dy;
        boolean moveX;
        int dx;
        boolean moveY;
        if (path.size() >= v) {
            Pathfinder.Node nextNode;
            if (path.size() >= v) {
                nextNode = path.get(v); // Le premier nœud est la position actuelle
            } else {
                nextNode =path.get(path.size() - 1);
            }
            int nextX = nextNode.x;
            int nextY = nextNode.y;

            dx = v * (footX < nextX ? 1 : -1);
            dy = v * (footY < nextY ? 1 : -1);

            moveX = Math.abs(footX - nextX) >= v;
            moveY = Math.abs(footY - nextY) >= v;

            if (moveX || moveY) {
                actGk.getSprite().incFrame();
                actGk.getSprite().setWalk();
                if (Math.abs(footX - nextX) > Math.abs(footY - nextY)) {
                    actGk.setDirection(dx > 0 ? 0 : 2);
                } else {
                    actGk.setDirection(dy > 0 ? 1 : 3);
                }
                actGk.setFoot(nextX, nextY);
            }
        }
    }

    private int invFootX(int x) {
        return x - (actGk.getSprite().getWidth() / 2);
    }

    private int invFootY(int y) {
        return y - actGk.getSprite().getHeight() + 1;
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
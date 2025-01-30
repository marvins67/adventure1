package com.iterevg.game.adventure1;

import javax.swing.*;

public class Main extends JPanel {
    private static Game game;

    public static void main(String[] args) {
        game = new Game();
        game.init();
        double avant = 0;
        double apres = System.nanoTime() / 1000000000.0;
        double duree = 0;
        double frameTime = 0;
        int frames = 0;
        int fps = 0;
        while (true) {
            avant = System.nanoTime() / 1000000000.0;
            game.run();
            duree = avant - apres;
            apres = avant;
            frameTime += duree;
            frames++;
            if (frameTime >= 1.0) {
                frameTime = 0;
                fps = frames;
                frames = 0;
                System.out.println("FPS : " + fps);
            }
        }
        //game.stop();
    }
}

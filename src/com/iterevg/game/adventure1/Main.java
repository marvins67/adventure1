package com.iterevg.game.adventure1;

import javax.swing.*;

public class Main extends JPanel {
    private static Game game;

    public static void main(String[] args) {
        game = new Game();
        game.init();
        game.run();
        game.stop();
    }
}

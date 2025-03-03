package com.marvins.adventure1;

import javax.swing.*;

public class Main extends JPanel {

    public static void main(String[] args) {
        Game game = new Game();
        game.init();
        game.run();
        game.stop();
    }
}
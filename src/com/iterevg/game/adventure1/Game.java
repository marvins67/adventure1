package com.iterevg.game.adventure1;

import com.iterevg.game.adventure1.graphics.Image;
import com.iterevg.game.adventure1.graphics.ScreenRenderer;
import com.iterevg.game.adventure1.graphics.Sprite;

import javax.swing.*;

public class Game {

    private JFrame frame ;
    private ScreenRenderer screenRenderer;

    private Image background;

    private Sprite sprite;

   public void init(){
       frame = new JFrame(Constants.NAME);
       screenRenderer = new ScreenRenderer(Constants.SCR_WIDTH, Constants.SCR_HEIGHT);
       frame.add(screenRenderer);
       frame.setSize(Constants.SCR_WIDTH, Constants.SCR_HEIGHT);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setResizable(false);
       frame.setVisible(true);

       background = new Image();
       background.read("image.jpg");

       Image image = new Image();
       image.read("sprite.png");
       sprite = new Sprite();
       sprite.setImage(image);
       sprite.setX(400);
       sprite.setY(400);
   }

   public void run(){
       screenRenderer.draw(background);
       screenRenderer.draw(sprite);
       screenRenderer.repaint();
       update();
   }

    private void update() {
       //Mise à jour des données du jeu
        sprite.setX(sprite.getX() + 1 );
        sprite.setY(sprite.getY() + 1 );
    }

    public void stop(){}
}

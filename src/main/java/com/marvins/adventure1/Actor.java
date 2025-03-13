package com.marvins.adventure1;

import com.marvins.adventure1.graphics.Sprite;

import java.awt.*;

import static com.marvins.adventure1.Constants.SPR_HEIGHT;
import static com.marvins.adventure1.Constants.SPR_WIDTH;

public class Actor {

    Sprite[] sprites = new Sprite[4]; // 473*(56*4) = 473*224
    Point position;
    Point foot;

    int direction = 0; //0 : droite ; 1 : bas ; 2 gauche ; 3 : haut

    public Actor() {
    }

    public void addSprites(String imagePath) {
        for (int i=0; i<4 ; i++) {
            Sprite sprite = new Sprite();
            sprite.getSpriteFromFile(imagePath, i);
            sprite.setWidth(SPR_WIDTH);
            sprite.setHeight(SPR_HEIGHT);
            this.sprites[i] = sprite;
        }
    }

    public void addSprite(String imagePath, int direction) {
        Sprite sprite = new Sprite();
        sprite.getSpriteFromFile(imagePath);
        sprite.setWidth(SPR_WIDTH);
        sprite.setHeight(SPR_HEIGHT);
        this.sprites[direction] = sprite;
    }

    public Sprite getSprite(){
        return sprites[direction];
    }

    public Sprite[] getSprites(){
        return sprites;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
        if (this.foot == null) {
            this.foot = new Point(0,0);
        }
        this.foot.x = (int)(this.position.getX() + (getSprite().getWidth() / 2));
        this.foot.y = (int)(this.position.getY() + getSprite().getHeight() - 1);
    }

    public Point getFoot() {
        return foot;
    }

    public void setFoot(int x, int y) {
        this.foot.x = x ;
        this.foot.y = y ;
        this.position.x = (x - (getSprite().getWidth() / 2));
        this.position.y = (y - getSprite().getHeight() + 1);
    }
}
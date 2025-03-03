package com.marvins.adventure1;

import com.marvins.adventure1.graphics.Position;
import com.marvins.adventure1.graphics.Sprite;

import static com.marvins.adventure1.Constants.SPR_HEIGHT;
import static com.marvins.adventure1.Constants.SPR_WIDTH;

public class Actor {

    Sprite[] sprites = new Sprite[4]; // 473*(56*4) = 473*224
    Position position;

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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
package com.iterevg.game.adventure1;

import com.iterevg.game.adventure1.graphics.Pixel;
import com.iterevg.game.adventure1.graphics.Position;
import com.iterevg.game.adventure1.graphics.Sprite;

import static com.iterevg.game.adventure1.Constants.SPR_HEIGHT;
import static com.iterevg.game.adventure1.Constants.SPR_WIDTH;

public class Actor {

    Sprite[] sprites = new Sprite[4];
    Position position;

    int direction = 0; //0 : droite ; 1 : bas ; 2 gauche ; 3 : haut

    public Actor() {
    }

    public void addSprite(String imagePath, int direction, boolean invert) {
        Sprite sprite = new Sprite();
        sprite.getSpriteFromFile(imagePath);
        sprite.setWidth(SPR_WIDTH);
        sprite.setHeight(SPR_HEIGHT);

        this.sprites[direction] = invert ? invertSprite(sprite) : sprite;
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

    private Sprite invertSprite(Sprite sprite) {
        Sprite invertedSprite = new Sprite();
        invertedSprite.setWidth(sprite.getWidth());
        invertedSprite.setHeight(sprite.getHeight());
        Pixel[][] invertedPixel = new Pixel[sprite.getWidth()][sprite.getHeight()];

        for (int j = 0; j < sprite.getHeight(); j++) {
            for (int i = 0; i < sprite.getWidth(); i++) {
                Pixel p = new Pixel(sprite.getPixels()[i][j].getRgbValue());
                invertedPixel[sprite.getWidth() - i - 1][j] = p;
            }
        }
        invertedSprite.setHeight(sprite.getHeight());
        invertedSprite.setWidth((sprite.getWidth()));
        invertedSprite.setPixels(invertedPixel);
        return invertedSprite;
    }
}

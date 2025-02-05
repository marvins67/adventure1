package com.iterevg.game.adventure1;

import com.iterevg.game.adventure1.graphics.Position;
import com.iterevg.game.adventure1.graphics.Sprite;

public class Actor {

    Sprite[] sprites = new Sprite[4];
    Position position;

    int direction = 0; //0 : droite ; 1 : bas ; 2 gauche ; 3 : haut

    public Sprite getSprite(){
        return sprites[direction];
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

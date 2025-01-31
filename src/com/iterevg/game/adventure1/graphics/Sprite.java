package com.iterevg.game.adventure1.graphics;

public class Sprite {
    private Image image;

    private int x;
    private int y;
    private int frame = 1;
    private boolean isWalking = false;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFrame() {
        return frame;
    }

    public void incFrame() {
        frame++;
        frame = frame % 10 +1 ;
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalk() {
        isWalking = true;
    }

    public void setNoWalk() {
        isWalking = false;
        frame = 0;
    }
}

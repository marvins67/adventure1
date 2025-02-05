package com.iterevg.game.adventure1.graphics;

public class Sprite {
    private Image image;
    private int width;
    private int height;
    private int frame = 1;
    private boolean isWalking = false;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getFrame() {
        return frame;
    }

    public void incFrame() {
        frame++;
        frame = frame % 10 + 1;
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

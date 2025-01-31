package com.iterevg.game.adventure1.graphics;

public class Pixel {
    int x;
    int y;
    int rgbValue;

    public Pixel(int x, int y, int rgbValue) {
        this.x = x;
        this.y = y;
        this.rgbValue = rgbValue;
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

    public int getRgbValue() {
        return rgbValue;
    }

    public void setRgbValue(int rgbValue) {
        this.rgbValue = rgbValue;
    }
}

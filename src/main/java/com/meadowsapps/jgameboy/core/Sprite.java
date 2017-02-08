package com.meadowsapps.jgameboy.core;

/**
 * Created by dmeadows on 2/8/2017.
 */
public class Sprite {

    private int x, y;
    private int height = 8;
    private int tileNumber;
    private int attributes;
    private boolean xFlip, yFlip;
    private boolean drawPriority;
    private int palette;

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

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTileNumber() {
        return tileNumber;
    }

    public void setTileNumber(int tileNumber) {
        this.tileNumber = tileNumber;
    }

    public int getAttributes() {
        return attributes;
    }

    public void setAttributes(int attributes) {
        this.attributes = attributes;
    }

    public boolean isXFlip() {
        return xFlip;
    }

    public void setXFlip(boolean xFlip) {
        this.xFlip = xFlip;
    }

    public boolean isYFlip() {
        return yFlip;
    }

    public void setYFlip(boolean yFlip) {
        this.yFlip = yFlip;
    }

    public boolean isDrawPriority() {
        return drawPriority;
    }

    public void setDrawPriority(boolean drawPriority) {
        this.drawPriority = drawPriority;
    }

    public int getPalette() {
        return palette;
    }

    public void setPalette(int palette) {
        this.palette = palette;
    }
}

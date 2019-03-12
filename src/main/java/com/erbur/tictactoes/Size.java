package com.erbur.tictactoes;

public class Size {
    private int width;
    private int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Size(Size otherSize) {
        this.width = otherSize.width;
        this.height = otherSize.height;
    }

    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }

    // Not equals - this doesn't check if its the same object, but if they are similar.
    public boolean compareTo(Size otherSize) {
        return this.width == otherSize.width && this.height == otherSize.height;
    }

    public String toString() {
        return String.format("(%s, %s)", width, height);
    }
}

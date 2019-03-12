package com.erbur.tictactoes;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point otherPoint) {
        this.x = otherPoint.x;
        this.y = otherPoint.y;
    }

    public int getX() { return this.x; }
    public int getY() { return this.y; }

    // At 0 on x or y-axis
    public boolean atZeroAxis(Size box) {
        return x == 0 || y == 0;
    }

    // At 0 on x or y-axis
    public boolean atBorders(Size box) {
        return x == 0 || y == 0 || x == box.getWidth()-1 || y == box.getHeight()-1;
    }

    // Not equals - this doesn't check if its the same object, but if they are similar.
    public boolean compareTo(Point otherPoint) {
        return this.x == otherPoint.x && this.y == otherPoint.y;
    }

    public boolean outside(Size box) {
        return x < 0 || y < 0 || x >= box.getWidth() || y >= box.getHeight();
    }

    public Point moveUp() {
        y--;
        return this;
    }

    public Point moveDown() {
        y++;
        return this;
    }

    public Point moveLeft() {
        x--;
        return this;
    }

    public Point moveRight() {
        x++;
        return this;
    }

    public String toString() {
        return String.format("(%s, %s)", x, y);
    }
}
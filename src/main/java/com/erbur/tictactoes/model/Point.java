package com.erbur.tictactoes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
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

    // At 0 on x or y-axis
    public boolean atZeroAxis() {
        return x == 0 || y == 0;
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
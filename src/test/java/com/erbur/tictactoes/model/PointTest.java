package com.erbur.tictactoes.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PointTest {

    @Test
    public void atZeroAxis() {
        Point pointX = new Point(0, 1);
        Point pointY = new Point(1, 0);

        assert(pointX.atZeroAxis());
        assert(pointY.atZeroAxis());
    }


    @Test
    public void outside() {
        Point pointX = new Point(4, 1);
        Point pointY = new Point(1, 4);
        Size board = new Size(3, 3);

        assert(pointX.outside(board));
        assert(pointY.outside(board));
    }

    @Test
    public void moveUp() {
        Point pointX = new Point(4, 1);
        pointX.moveUp();
        assertThat(pointX).isEqualTo(new Point(4, 0));
    }

    @Test
    public void moveDown() {
        Point pointX = new Point(4, 1);
        pointX.moveDown();
        assertThat(pointX).isEqualTo(new Point(4, 2));
    }

    @Test
    public void moveLeft() {
        Point pointX = new Point(4, 1);
        pointX.moveLeft();
        assertThat(pointX).isEqualTo(new Point(3, 1));
    }

    @Test
    public void moveRight() {
        Point pointX = new Point(4, 1);
        pointX.moveRight();
        assertThat(pointX).isEqualTo(new Point(5, 1));
    }
}
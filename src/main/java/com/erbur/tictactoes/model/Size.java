package com.erbur.tictactoes.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
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

    // Not equals - this doesn't check if its the same object, but if they are similar.
    public boolean compareTo(Size otherSize) {
        return this.width == otherSize.width && this.height == otherSize.height;
    }

    public String toString() {
        return String.format("(%s, %s)", width, height);
    }
}

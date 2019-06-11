package com.erbur.tictactoes.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
public class Size {
    private int width;
    private int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String toString() {
        return String.format("(%s, %s)", width, height);
    }
}

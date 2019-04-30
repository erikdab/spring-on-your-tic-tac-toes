package com.erbur.tictactoes.model;

import com.erbur.tictactoes.model.enums.Token;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Data
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Board {
    //[row][column]
    private Token[][] fields;

    private Size size;

    public Board(int width, int height) {
        size = new Size(width, height);

        resetBoard();
    }

    public void resetBoard() {
        fields = new Token[size.getHeight()][size.getWidth()];
        for (int x = 0; x < size.getHeight(); x++) {
            for (int y = 0; y < size.getWidth(); y++) {
                fields[y][x] = Token.Blank;
            }
        }
    }

    public char[][] getFieldsChar() {
        char[][] fieldsChar = new char[size.getHeight()][size.getWidth()];
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                fieldsChar[y][x] = fields[y][x].toChar();
            }
        }
        return fieldsChar;
    }

    public void setFields(Token[][] fields) {
        int width = fields[0].length;
        int height = fields.length;

        if (width != size.getWidth() || height != size.getHeight())
            throw new IllegalArgumentException("Passed Field Board Size.");

        this.fields = fields;
    }

    public Token getField(Point location) {
        return fields[location.getY()][location.getX()];
    }

    public Token getField(int x, int y) {
        return fields[y][x];
    }

    public void setField(Point location, Token token) {
        if (getField(location) != Token.Blank) throw new IllegalArgumentException("Cannot play over a non-blank!");
        fields[location.getY()][location.getX()] = token;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Board %s", size));
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                result.append(fields[y][x]);
            }
            result.append("\n");
        }
        return result.toString();
    }
}

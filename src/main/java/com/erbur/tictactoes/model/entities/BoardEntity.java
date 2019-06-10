package com.erbur.tictactoes.model.entities;

import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.Size;
import com.erbur.tictactoes.model.enums.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
public class BoardEntity {
    //[row][column]
    private Token[][] fields;

    private Size size;

    public BoardEntity(int width, int height) {
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

    public Token[][] getFields() {
        return this.fields.clone();
    }

    public void setFields(Token[][] fields) {
        int width = fields[0].length;
        int height = fields.length;

        if (width != size.getWidth() || height != size.getHeight())
            throw new IllegalArgumentException("Passed Field BoardEntity Size.");

        this.fields = new Token[size.getHeight()][size.getWidth()];

        for (int x = 0; x < fields.length; x++) {
            for (int y = 0; y < fields[x].length; y++) {
                this.fields[y][x] = fields[y][x];
            }
        }
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
        result.append(String.format("BoardEntity %s", size));
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                result.append(fields[y][x]);
            }
            result.append("\n");
        }
        return result.toString();
    }
}

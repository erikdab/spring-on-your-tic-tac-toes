package com.erbur.tictactoes.model;

public class Board {
    //[row][column]
    private Token[][] fields;

    private Size size;

    public Board(int width, int height) {
        size = new Size(width, height);

        resetBoard();
    }

    public Board(Board otherBoard) {
        size = new Size(otherBoard.size.getWidth(), otherBoard.size.getHeight());

        resetBoard();
    }

    public Board(Token[][] fields) {
        int width = fields[0].length;
        int height = fields.length;
        size = new Size(width, height);
    }

    public Size getSize() { return size; }

    public void setFields(Token[][] fields) {
        int width = fields[0].length;
        int height = fields.length;

        if(width != size.getWidth() || height != size.getHeight())
            throw new IllegalArgumentException("Passed Field Board Size.");

        this.fields = fields;
    }

    public Token[][] getFields() { return fields; }

    public void resetBoard() {
        fields = new Token[size.getHeight()][size.getWidth()];
        for(int x=0;x<size.getHeight();x++) {
            for(int y=0;y<size.getWidth();y++) {
                fields[y][x] = Token.Blank;
            }
        }
    }

    // Validation at some point (if its a blank or not)
    public void setField(Point location, Token token) {
        if(getField(location) != Token.Blank) throw new IllegalArgumentException("Cannot play over a non-blank!");
        fields[location.getY()][location.getX()] = token;
    }

    public void setField(int x, int y, Token token) {
        fields[y][x] = token;
    }

    public Token getField(Point location) {
        return fields[location.getY()][location.getX()];
    }

    public Token getField(int x, int y) {
        return fields[y][x];
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("Board %s", size));
        for(int x=0;x<size.getWidth();x++) {
            for(int y=0;y<size.getHeight();y++) {
                result.append(fields[y][x]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    public char[][] getFieldsChar() {
        char [][]fieldsChar = new char[size.getHeight()][size.getWidth()];
        for(int x = 0; x < size.getWidth(); x++) {
            for(int y = 0;  y < size.getHeight(); y++) {
                fieldsChar[y][x] = fields[y][x].toChar();
            }
        }
        return fieldsChar;
    }
}

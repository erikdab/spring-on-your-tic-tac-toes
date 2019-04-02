package com.erbur.tictactoes.model;

public enum Token {
    Blank,
    X,
    O;

    public char toChar() {
        switch (this) {
            case X:
                return 'X';
            case O:
                return 'O';
            default:
                return ' ';
        }
    }

    public String toString() {
        return "" + toChar();
    }

    public static Token fromChar(char token) {
        switch (token) {
            case 'X':
                return Token.X;
            case 'O':
                return Token.O;
            default:
                return Token.Blank;
        }
    }
}

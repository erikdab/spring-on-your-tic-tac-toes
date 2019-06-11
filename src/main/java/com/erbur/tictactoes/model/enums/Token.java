package com.erbur.tictactoes.model.enums;

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
}

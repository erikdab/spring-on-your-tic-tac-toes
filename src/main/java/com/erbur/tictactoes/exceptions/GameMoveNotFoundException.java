package com.erbur.tictactoes.exceptions;

public class GameMoveNotFoundException extends Exception {
    public GameMoveNotFoundException(String s) {
        super(s);
    }
}
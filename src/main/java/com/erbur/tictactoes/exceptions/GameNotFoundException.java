package com.erbur.tictactoes.exceptions;

public class GameNotFoundException extends Exception {
    public GameNotFoundException(String s) {
        super(s);
    }
}
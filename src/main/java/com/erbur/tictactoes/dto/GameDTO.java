package com.erbur.tictactoes.dto;

import com.erbur.tictactoes.model.Game;

public class GameDTO {
    private String player1Name;
    private char player1Token;
    private String player2Name;
    private char player2Token;
    private String currentPlayer;

    private int boardLength;
    private int winLineLength;

    public GameDTO() { }

    public GameDTO(String player1Name, char player1Token, String player2Name, char player2Token, int boardLength, int winLineLength) {
        this.setPlayer1Name(player1Name);
        this.setPlayer2Name(player2Name);
        this.setPlayer1Token(player1Token);
        this.setPlayer2Token(player2Token);
        this.setBoardLength(boardLength);
        this.setWinLineLength(winLineLength);
    }

    public GameDTO(Game game) {
        this.setPlayer1Name(game.getFirstPlayer().getName());
        this.setPlayer2Name(game.getSecondPlayer().getName());
        this.setCurrentPlayer(game.getCurrentPlayer().getName());
        this.setPlayer1Token(game.getFirstPlayerToken().toChar());
        this.setPlayer2Token(game.getSecondPlayerToken().toChar());
        this.setBoardLength(game.getBoardLength());
        this.setWinLineLength(game.getWinLineLength());
    }

    public String getPlayer1Name() {
        return player1Name;
    }

    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    public char getPlayer1Token() {
        return player1Token;
    }

    public void setPlayer1Token(char player1Token) {
        this.player1Token = player1Token;
    }

    public String getPlayer2Name() {
        return player2Name;
    }

    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    public char getPlayer2Token() {
        return player2Token;
    }

    public void setPlayer2Token(char player2Token) {
        this.player2Token = player2Token;
    }

    public int getBoardLength() {
        return boardLength;
    }

    public void setBoardLength(int boardLength) {
        this.boardLength = boardLength;
    }

    public int getWinLineLength() {
        return winLineLength;
    }

    public void setWinLineLength(int winLineLength) {
        this.winLineLength = winLineLength;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}

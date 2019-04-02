package com.erbur.tictactoes.dto;

import com.erbur.tictactoes.model.Game;

public class GameStatusDTO {
    private boolean isWon;
    private boolean isDraw;
    private String playerWonName;
    private String playerWonToken;

    private int moveCount;

    private BoardDTO board;

    public GameStatusDTO() {}
    public GameStatusDTO(boolean isWon, boolean isDraw, String playerWonName, String playerWonToken, int moveCount) {
        this.setWon(isWon);
        this.setDraw(isDraw);
        this.setPlayerWonName(playerWonName);
        this.setPlayerWonToken(playerWonToken);
        this.setMoveCount(moveCount);
    }

    public GameStatusDTO(Game game) {
        this.setWon(game.isBoardWon());
        this.setDraw(game.isBoardDraw());
        if(isWon()) {
            this.setPlayerWonName(game.getWinner().getName());
            this.setPlayerWonToken(game.getWinnerToken().toString());
        }
        this.setMoveCount(game.getMoveCount());
        this.setBoard(new BoardDTO(game.getBoard()));
    }

    public boolean isWon() {
        return isWon;
    }

    public void setWon(boolean won) {
        isWon = won;
    }

    public boolean isDraw() {
        return isDraw;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public String getPlayerWonName() {
        return playerWonName;
    }

    public void setPlayerWonName(String playerWonName) {
        this.playerWonName = playerWonName;
    }

    public String getPlayerWonToken() {
        return playerWonToken;
    }

    public void setPlayerWonToken(String playerWonToken) {
        this.playerWonToken = playerWonToken;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public BoardDTO getBoard() {
        return board;
    }

    public void setBoard(BoardDTO board) {
        this.board = board;
    }
}

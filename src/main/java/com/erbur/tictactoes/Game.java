package com.erbur.tictactoes;

import java.util.*;

public class Game {
    private Board board;

    private List<Player> players;

    private Map<Player, Token> playerTokenMap = new HashMap<>();

    // Should these be placed in board or maybe create a Game class (only data - logic here)
    private boolean boardWon = false;
    private boolean boardDraw = false;
    private int moveCount = 0;
    private Player winner = null;

    private Player firstPlayer;

    private int boardLength;
    private int winLineLength;

    // Another constructor with board?
    public Game(int boardLength, int winLineLength, Player[] players, Player firstPlayer, Player playerX) {
        if(boardLength < 3)
            throw new IllegalArgumentException("Board Length must be greater than 3 or the game would be too easy!");

        if(winLineLength > boardLength)
            throw new IllegalArgumentException("Win Line Length cannot be greater than Board Length or there would be no way to win!");
        else if(winLineLength < 3)
            throw new IllegalArgumentException("Win Line Length must be greater than 3 or the game would be too easy!");

        this.boardLength = boardLength;
        this.board = new Board(boardLength, boardLength);
        this.winLineLength = winLineLength;

        if(players.length != 2) throw new IllegalArgumentException("Each TicTacToe game must have exactly 2 players!");
        this.players = new ArrayList<>(Arrays.asList(players));

        if(! this.players.contains(firstPlayer) || ! this.players.contains(playerX)) {
            throw new IllegalArgumentException("First Player and Player X must be players actually playing the game!");
        }

        this.firstPlayer = firstPlayer;

        this.playerTokenMap.put(playerX, Token.X);
        for(Player player : this.players) {
            if(player != playerX) {
                this.playerTokenMap.put(player, Token.O);
            }
        }
    }

    public void setBoardFields(Token[][] fields) {
        board.setFields(fields);
        moveCount = 0;
        for(Token[] row : fields) {
            for(Token cell : row) {
                if(cell != Token.Blank) moveCount++;
            }
        }
    }

    public void setWinner(Player player) {
        this.winner = player;
        this.boardWon = true;
    }

    public void setDraw() {
        this.boardDraw = true;
    }

    public boolean isBoardDraw() {
        return boardDraw;
    }

    public void addMove(Point move, Player player) {
        if(move.outside(board.getSize())) throw new IllegalArgumentException("Move is outside box!");
        board.setField(move, getTokenFor(player));
    }

    public Token getTokenFor(Player player) {
        return playerTokenMap.get(player);
    }

    public Player getPlayerFor(Token token) {
        for(Player player : players) {
            if(getTokenFor(player) == token) return player;
        }
        throw new IllegalStateException(String.format("Game does not have a player for token: %s", token));
    }

    public boolean isBoardWon() {
        return boardWon;
    }

    public Player getWinner() {
        return winner;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public Player[] getPlayers() {
        Player[] playersArr = new Player[players.size()];

        return players.toArray(playersArr);
    }

    public Board getBoard() {
        return board;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public int getBoardLength() {
        return boardLength;
    }

    public int getWinLineLength() {
        return winLineLength;
    }
}

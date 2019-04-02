package com.erbur.tictactoes.logic;

import com.erbur.tictactoes.model.*;
import org.springframework.stereotype.Service;

@Service()
public class TicTacToesGame {
    private Game game;

    // Other things to consider: history, maybe put check victory condition in Board (maybe not)

    public TicTacToesGame() {
    }

    // Pass more params in new game.
    public void newGame(int boardLength, int winLineLength) {
        // These could be in the database
        // They can have statistics of how many victories / etc.
        Player[]players = new Player[]{ new Player("Frank"), new Player("Erik") };

        game = new Game(boardLength, winLineLength, players, players[0], players[1]);
    }

    public void makeMove(Point point, Player player) {
        game.addMove(point, player);

        checkVictoryConditionAfterMove(point, player);
    }

    public Game getGame() {
        return game;
    }

    // horizontal or vertical
    private boolean checkWinHorizontal(Point move, Token token, boolean horizontal) {
        int x = move.getX();
        int y = move.getY();
        Board board = game.getBoard();
        int boardLength = game.getBoardLength();
        int winLineLength = game.getWinLineLength();

        int count = 0;
        for(int i = 0; i < boardLength; i++) {
            Token currentToken = horizontal ? board.getField(i, y) : board.getField(x, i);
            if(currentToken == token) count++;
            else count = 0;

            if(count >= winLineLength) break;
        }
        return count >= winLineLength;
    }

    private boolean legalDiagonal(Point move, boolean diagonal) {
        int x =  move.getX();
        int y = move.getY();
        int boardLength = game.getBoardLength();
        int winLineLength = game.getWinLineLength();
        int leftoverSpace = boardLength - winLineLength;
        int spacePast = boardLength + leftoverSpace;

        if(diagonal) return x == y || x-y <= leftoverSpace && y-x <= leftoverSpace;
        else return x+y >= winLineLength || x+y <= spacePast;
    }

    // diagonal or antidiagonal
    private boolean checkWinDiagonal(Point move, Token token, boolean diagonal) {
        if(! legalDiagonal(move, diagonal)) return false;

        Board board = game.getBoard();
        Size boardSize = board.getSize();
        int winLineLength = game.getWinLineLength();

        // Find start point on x or y axis
        Point location = new Point(move);
        while(! location.atZeroAxis(boardSize)) {
            location.moveUp();
            if(diagonal) location.moveLeft();
            else location.moveRight();
        }
        // Travel through entire diagonal and see if it is completed.
        int count = 0;
        while(! location.outside(boardSize)) {
            if(board.getField(location) == token)
                count++;
            else count = 0;

            if(count >= winLineLength) break;

            location.moveDown();
            if(diagonal) location.moveRight();
            else location.moveLeft();
        }
        return count >= winLineLength;
    }

    private boolean checkDraw() {
        int boardLength = game.getBoardLength();

        return game.getMoveCount() >= (Math.pow(boardLength, 2) - 1);
    }

    // I could easily make this work for checking victory condition whenever,
    // not just after a move (but rather checking if the board is won simply, and by whom).
    private void checkVictoryConditionAfterMove(Point move, Player player) {
        Token token = game.getTokenFor(player);

        // check column - horizontal
        if(checkWinHorizontal(move, token, true)) {
            game.setWinner(player);
            return;
        }

        // check row - vertical
        if(checkWinHorizontal(move, token, false)) {
            game.setWinner(player);
            return;
        }

        // check diagonal, but only if the move was on a legal diagonal
        if(checkWinDiagonal(move, token, true)) {
            game.setWinner(player);
            return;
        }

        // check anti-diagonal, but only if the move was on a legal anti-diagonal
        if(checkWinDiagonal(move, token, false)) {
            game.setWinner(player);
            return;
        }

        // check draw - if all moves were used up and a win did not occur
        if(checkDraw()) game.setDraw();
    }
}

package com.erbur.tictactoes.unit;

import com.erbur.tictactoes.logic.TicTacToesGame;
import com.erbur.tictactoes.model.Game;
import com.erbur.tictactoes.model.Player;
import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.Token;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicTacToesGameTest {
    private int BOARD_LENGTH = 5, WIN_LINE_LENGTH = 4;
    private Point validMove = new Point(2, 4);
    private Token[][] almostWin = new Token[][]{
            {Token.Blank, Token.X, Token.Blank, Token.Blank, Token.X},
            {Token.Blank, Token.Blank, Token.X, Token.X, Token.Blank},
            {Token.O, Token.Blank, Token.X, Token.X, Token.Blank},
            {Token.O, Token.Blank, Token.Blank, Token.Blank, Token.Blank},
            {Token.O, Token.O, Token.O, Token.Blank, Token.Blank}
    };
    private Token [][] almostDraw = new Token[][]{
            {Token.X, Token.X, Token.O, Token.O, Token.X},
            {Token.X, Token.O, Token.X, Token.X, Token.O},
            {Token.O, Token.X, Token.X, Token.X, Token.O},
            {Token.O, Token.O, Token.O, Token.X, Token.O},
            {Token.O, Token.O, Token.X, Token.Blank, Token.X}
    };
    private Point playerXWinMoveDiagonal = new Point(4, 3), playerOWinMoveHorizontal = new Point(3,4);
    private Point playerXWinMoveAntiDiagonal = new Point(1, 3), playerOWinMoveVertical = new Point(0,1);
    private Point playerXNoWinMove = new Point(4,4);
    private Point playerODrawMove = new Point(3,4);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test public void newGame_BoardAndWinLength() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);

        Game game = ticTacToesGame.getGame();

        assertThat(game.getBoardLength()).isEqualTo(BOARD_LENGTH);
        assertThat(game.getWinLineLength()).isEqualTo(WIN_LINE_LENGTH);
    }

    @Test public void makeMove_Valid() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);

        Game game = ticTacToesGame.getGame();
        Player[]players = ticTacToesGame.getGame().getPlayers();
        Player player = players[0];
        Token token = game.getTokenFor(player);

        ticTacToesGame.makeMove(validMove, player);
        assertThat(game.getBoard().getField(validMove)).isEqualTo(token);
    }

    @Test public void makeMove_OutsideBox_ExceptionThrown() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);

        Game game = ticTacToesGame.getGame();
        Player []players = ticTacToesGame.getGame().getPlayers();
        Player player = players[0];

        Point move = new Point(0,BOARD_LENGTH+2);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Move is outside box!");
        ticTacToesGame.makeMove(move, player);
    }

    @Test public void makeMove_OverANonBlank_ExceptionThrown() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);

        Game game = ticTacToesGame.getGame();
        Player []players = ticTacToesGame.getGame().getPlayers();
        Player player = players[0];

        Point move = new Point(0,BOARD_LENGTH+2);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Move is outside box!");
        ticTacToesGame.makeMove(move, player);
    }

    @Test public void makeMove_WinPlayerXDiagonal() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGame.getGame();
        game.setBoardFields(almostWin);

        Player player = game.getPlayerFor(Token.X);

        ticTacToesGame.makeMove(playerXWinMoveDiagonal, player);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(player);
    }

    @Test public void makeMove_WinPlayerXAntiDiagonal() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGame.getGame();
        game.setBoardFields(almostWin);

        Player player = game.getPlayerFor(Token.X);

        ticTacToesGame.makeMove(playerXWinMoveAntiDiagonal, player);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(player);
    }

    @Test public void makeMove_WinPlayerOHorizontal() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGame.getGame();
        game.setBoardFields(almostWin);

        // Player X must play first, then Player 0 can go.
        Player playerX = game.getPlayerFor(Token.X);

        ticTacToesGame.makeMove(playerXNoWinMove, playerX);

        Player playerO = game.getPlayerFor(Token.O);

        ticTacToesGame.makeMove(playerOWinMoveHorizontal, playerO);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(playerO);
    }

    @Test public void makeMove_NotCurrentPlayer() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGame.getGame();
        game.setBoardFields(almostWin);

        Player player = game.getPlayerFor(Token.O);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Player Frank (O) cannot play now since it is player Erik's (X) turn.");
        ticTacToesGame.makeMove(playerOWinMoveVertical, player);
    }

    @Test public void makeMove_WinPlayerOVertical() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGame.getGame();
        game.setBoardFields(almostWin);

        // Player X must play first, then Player 0 can go.
        Player playerX = game.getPlayerFor(Token.X);

        ticTacToesGame.makeMove(playerXNoWinMove, playerX);

        Player playerO = game.getPlayerFor(Token.O);

        ticTacToesGame.makeMove(playerOWinMoveVertical, playerO);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(playerO);
    }

    @Test public void makeMove_DrawPlayerO() {
        TicTacToesGame ticTacToesGame = new TicTacToesGame();

        ticTacToesGame.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGame.getGame();
        game.setBoardFields(almostDraw);
        System.out.println(game.getMoveCount());

        Player player = game.getPlayerFor(Token.O);

        ticTacToesGame.makeMove(playerODrawMove, player);
        assertThat(game.isBoardDraw()).isEqualTo(true);
        assertThat(game.isBoardWon()).isEqualTo(false);
        assertThat(game.getWinner()).isNull();
    }
}

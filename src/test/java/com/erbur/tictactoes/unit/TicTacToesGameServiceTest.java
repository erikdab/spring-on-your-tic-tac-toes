package com.erbur.tictactoes.unit;

import com.erbur.tictactoes.service.TicTacToesGameService;
import com.erbur.tictactoes.model.Game;
import com.erbur.tictactoes.model.Player;
import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.enums.Token;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class TicTacToesGameServiceTest {
    private int BOARD_LENGTH = 5, WIN_LINE_LENGTH = 4;
    private Point validMove = new Point(2, 4);
    private Token[][] almostWin = new Token[][]{
            {Token.Blank, Token.X, Token.Blank, Token.Blank, Token.X},
            {Token.Blank, Token.Blank, Token.X, Token.X, Token.Blank},
            {Token.O, Token.Blank, Token.X, Token.X, Token.Blank},
            {Token.O, Token.Blank, Token.Blank, Token.Blank, Token.Blank},
            {Token.O, Token.O, Token.O, Token.Blank, Token.Blank}
    };
    private Token[][] almostDraw = new Token[][]{
            {Token.X, Token.X, Token.O, Token.O, Token.X},
            {Token.X, Token.O, Token.X, Token.X, Token.O},
            {Token.O, Token.X, Token.X, Token.X, Token.O},
            {Token.O, Token.O, Token.O, Token.X, Token.O},
            {Token.O, Token.O, Token.X, Token.Blank, Token.X}
    };
    private Point playerXWinMoveDiagonal = new Point(4, 3), playerOWinMoveHorizontal = new Point(3, 4);
    private Point playerXWinMoveAntiDiagonal = new Point(1, 3), playerOWinMoveVertical = new Point(0, 1);
    private Point playerXNoWinMove = new Point(4, 4);
    private Point playerODrawMove = new Point(3, 4);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void newGame_BoardAndWinLength() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);

        Game game = ticTacToesGameService.getGame();

        assertThat(game.getBoardLength()).isEqualTo(BOARD_LENGTH);
        assertThat(game.getWinLineLength()).isEqualTo(WIN_LINE_LENGTH);
    }

    @Test
    public void makeMove_Valid() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);

        Game game = ticTacToesGameService.getGame();
        Player[] players = ticTacToesGameService.getGame().getPlayers();
        Player player = players[0];
        Token token = game.getTokenFor(player);

        ticTacToesGameService.makeMove(validMove, player);
        assertThat(game.boardGet().getField(validMove)).isEqualTo(token);
    }

    @Test
    public void makeMove_OutsideBox_ExceptionThrown() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);

        Game game = ticTacToesGameService.getGame();
        Player[] players = ticTacToesGameService.getGame().getPlayers();
        Player player = players[0];

        Point move = new Point(0, BOARD_LENGTH + 2);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Move is outside box!");
        ticTacToesGameService.makeMove(move, player);
    }

    @Test
    public void makeMove_OverANonBlank_ExceptionThrown() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);

        Game game = ticTacToesGameService.getGame();
        Player[] players = ticTacToesGameService.getGame().getPlayers();
        Player player = players[0];

        Point move = new Point(0, BOARD_LENGTH + 2);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Move is outside box!");
        ticTacToesGameService.makeMove(move, player);
    }

    @Test
    public void makeMove_WinPlayerXDiagonal() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGameService.getGame();
        game.setBoardFields(almostWin);

        Player player = game.getPlayerFor(Token.X);

        ticTacToesGameService.makeMove(playerXWinMoveDiagonal, player);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(player);
    }

    @Test
    public void makeMove_WinPlayerXAntiDiagonal() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGameService.getGame();
        game.setBoardFields(almostWin);

        Player player = game.getPlayerFor(Token.X);

        ticTacToesGameService.makeMove(playerXWinMoveAntiDiagonal, player);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(player);
    }

    @Test
    public void makeMove_WinPlayerOHorizontal() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGameService.getGame();
        game.setBoardFields(almostWin);

        // Player X must play first, then Player 0 can go.
        Player playerX = game.getPlayerFor(Token.X);

        ticTacToesGameService.makeMove(playerXNoWinMove, playerX);

        Player playerO = game.getPlayerFor(Token.O);

        ticTacToesGameService.makeMove(playerOWinMoveHorizontal, playerO);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(playerO);
    }

    @Test
    public void makeMove_NotCurrentPlayer() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGameService.getGame();
        game.setBoardFields(almostWin);

        Player player = game.getPlayerFor(Token.O);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Player Frank (O) cannot play now since it is player Erik's (X) turn.");
        ticTacToesGameService.makeMove(playerOWinMoveVertical, player);
    }

    @Test
    public void makeMove_WinPlayerOVertical() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGameService.getGame();
        game.setBoardFields(almostWin);

        // Player X must play first, then Player 0 can go.
        Player playerX = game.getPlayerFor(Token.X);

        ticTacToesGameService.makeMove(playerXNoWinMove, playerX);

        Player playerO = game.getPlayerFor(Token.O);

        ticTacToesGameService.makeMove(playerOWinMoveVertical, playerO);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(playerO);
    }

    @Test
    public void makeMove_DrawPlayerO() {
        TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

        ticTacToesGameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        Game game = ticTacToesGameService.getGame();
        game.setBoardFields(almostDraw);
        System.out.println(game.getMoveCount());

        Player player = game.getPlayerFor(Token.O);

        ticTacToesGameService.makeMove(playerODrawMove, player);
        assertThat(game.isBoardDraw()).isEqualTo(true);
        assertThat(game.isBoardWon()).isEqualTo(false);
        assertThat(game.getWinner()).isNull();
    }
}

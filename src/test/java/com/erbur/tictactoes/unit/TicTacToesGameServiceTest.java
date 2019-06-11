package com.erbur.tictactoes.unit;

import com.erbur.tictactoes.exceptions.PlayerNotFoundException;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.repository.GameMoveRepository;
import com.erbur.tictactoes.repository.GameRepository;
import com.erbur.tictactoes.repository.PlayerRepository;
import com.erbur.tictactoes.service.GameService;
import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.enums.Token;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.Optional;

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

    private GameRepository gameRepository;
    private GameMoveRepository gameMoveRepository;
    private PlayerRepository playerRepository;

    @Before
    public void setup() {
        GameEntity gameEntity = new GameEntity();

        Optional<GameEntity> optGameEntity = Optional.of(gameEntity);

        gameRepository = Mockito.mock(GameRepository.class);
        Mockito.when(gameRepository.findById(1L)).thenReturn(optGameEntity);

        gameMoveRepository = Mockito.mock(GameMoveRepository.class);

        PlayerEntity player1 = new PlayerEntity();
        player1.setName("Erik");

        PlayerEntity player2 = new PlayerEntity();
        player2.setName("Jonathan");

        playerRepository = Mockito.mock(PlayerRepository.class);

        Mockito.when(playerRepository.findById(1L)).thenReturn(Optional.of(player1));
        Mockito.when(playerRepository.findById(2L)).thenReturn(Optional.of(player2));
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void newGame_BoardAndWinLength() throws PlayerNotFoundException {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame2(1L, 2L, Token.O, BOARD_LENGTH, WIN_LINE_LENGTH);

        GameEntity game = gameService.getGame();

        assertThat(game.getBoardLength()).isEqualTo(BOARD_LENGTH);
        assertThat(game.getWinLineLength()).isEqualTo(WIN_LINE_LENGTH);
    }

    @Test
    public void makeMove_Valid() throws PlayerNotFoundException {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame2(1L, 2L, Token.O, BOARD_LENGTH, WIN_LINE_LENGTH);

        GameEntity game = gameService.getGame();
        PlayerEntity player = gameService.getGame().getFirstPlayer();
        Token token = game.getTokenFor(player);

        gameService.makeMove(validMove);
        assertThat(game.boardGet().getField(validMove)).isEqualTo(token);
    }

    @Test
    public void makeMove_OutsideBox_ExceptionThrown() throws PlayerNotFoundException {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame2(1L, 2L, Token.O, BOARD_LENGTH, WIN_LINE_LENGTH);

        Point move = new Point(0, BOARD_LENGTH + 2);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Move is outside box!");
        gameService.makeMove(move);
    }

    @Test
    public void makeMove_OverANonBlank_ExceptionThrown() throws PlayerNotFoundException {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame2(1L, 2L, Token.O, BOARD_LENGTH, WIN_LINE_LENGTH);

        Point move = new Point(0, BOARD_LENGTH + 2);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Move is outside box!");
        gameService.makeMove(move);
    }

    @Test
    public void makeMove_WinPlayerXDiagonal() {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        GameEntity game = gameService.getGame();
        game.setBoardFields(almostWin);

        PlayerEntity player = game.getPlayerFor(Token.X);

        gameService.makeMove(playerXWinMoveDiagonal, player);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(player);
    }

    @Test
    public void makeMove_WinPlayerXAntiDiagonal() {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        GameEntity game = gameService.getGame();
        game.setBoardFields(almostWin);

        PlayerEntity player = game.getPlayerFor(Token.X);

        gameService.makeMove(playerXWinMoveAntiDiagonal, player);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(player);
    }

    @Test
    public void makeMove_WinPlayerOHorizontal() {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        GameEntity game = gameService.getGame();
        game.setBoardFields(almostWin);

        // PlayerEntity X must play first, then PlayerEntity 0 can go.
        PlayerEntity playerX = game.getPlayerFor(Token.X);

        gameService.makeMove(playerXNoWinMove, playerX);

        PlayerEntity playerO = game.getPlayerFor(Token.O);

        gameService.makeMove(playerOWinMoveHorizontal, playerO);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(playerO);
    }

    @Test
    public void makeMove_NotCurrentPlayer() {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        GameEntity game = gameService.getGame();
        game.setBoardFields(almostWin);

        PlayerEntity player = game.getPlayerFor(Token.O);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("PlayerEntity Frank (O) cannot play now since it is player Erik's (X) turn.");
        gameService.makeMove(playerOWinMoveVertical, player);
    }

    @Test
    public void makeMove_WinPlayerOVertical() {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        GameEntity game = gameService.getGame();
        game.setBoardFields(almostWin);

        // PlayerEntity X must play first, then PlayerEntity 0 can go.
        PlayerEntity playerX = game.getPlayerFor(Token.X);

        gameService.makeMove(playerXNoWinMove, playerX);

        PlayerEntity playerO = game.getPlayerFor(Token.O);

        gameService.makeMove(playerOWinMoveVertical, playerO);
        assertThat(game.isBoardDraw()).isEqualTo(false);
        assertThat(game.isBoardWon()).isEqualTo(true);
        assertThat(game.getWinner()).isEqualTo(playerO);
    }

    @Test
    public void makeMove_DrawPlayerO() {
        GameService gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        gameService.newGame(BOARD_LENGTH, WIN_LINE_LENGTH);
        GameEntity game = gameService.getGame();
        game.setBoardFields(almostDraw);
        System.out.println(game.getMoveCount());

        PlayerEntity player = game.getPlayerFor(Token.O);

        gameService.makeMove(playerODrawMove, player);
        assertThat(game.isBoardDraw()).isEqualTo(true);
        assertThat(game.isBoardWon()).isEqualTo(false);
        assertThat(game.getWinner()).isNull();
    }
}

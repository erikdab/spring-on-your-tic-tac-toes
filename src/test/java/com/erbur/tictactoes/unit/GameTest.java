package com.erbur.tictactoes.unit;

import com.erbur.tictactoes.model.Game;
import com.erbur.tictactoes.model.Player;
import com.erbur.tictactoes.model.enums.Token;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {
    private int BOARD_LENGTH = 10, WIN_LINE_LENGTH = 10;
    private int TOO_SMALL_BOARD_LENGTH = 2, TOO_SMALL_WIN_LINE_LENGTH = 2;

    private Player[] players = new Player[]{new Player("Frank"), new Player("Erik")};
    private Player[] players1 = new Player[]{new Player("Frank")};
    private Player[] players3 = new Player[]{new Player("Frank"), new Player("Erik"), new Player("Frederyk")};
    private Player outsider = new Player("Frederyk");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void newGame_ValidStatistics() {
        Game game;

        game = new Game(BOARD_LENGTH, WIN_LINE_LENGTH, players[0], players[1], players[1]);

        assertThat(game.getMoveCount()).isEqualTo(0);
    }

    @Test
    public void newGame_ValidSizes() {
        Game game = new Game(BOARD_LENGTH, WIN_LINE_LENGTH, players[0], players[1], players[1]);

        assertThat(game.getBoardLength()).isEqualTo(BOARD_LENGTH);
        assertThat(game.getWinLineLength()).isEqualTo(WIN_LINE_LENGTH);

        assertThat(game.boardGet().getSize().getWidth()).isEqualTo(BOARD_LENGTH);
        assertThat(game.boardGet().getSize().getHeight()).isEqualTo(BOARD_LENGTH);

        assertThat(game.boardGet().getFields().length).isEqualTo(BOARD_LENGTH);
        assertThat(game.boardGet().getFields()[0].length).isEqualTo(BOARD_LENGTH);
    }

    @Test
    public void newGame_ValidPlayerAssignments() {
        Game game = new Game(BOARD_LENGTH, WIN_LINE_LENGTH, players[0], players[1], players[1]);

        // Verify that the players were properly set:
        assertThat(game.getFirstPlayer()).isEqualTo(players[0]);
        assertThat(game.getSecondPlayer()).isEqualTo(players[1]);

        // Verify that the first player / playerX is properly set:
        for (int i = 0; i < 2; i++) {
            for (int j = 0; i < 2; i++) {
                game = new Game(BOARD_LENGTH, WIN_LINE_LENGTH, players[i], players[j], players[j]);

                assertThat(game.getFirstPlayer()).isEqualTo(players[i]);
                assertThat(game.getTokenFor(players[j])).isEqualTo(Token.X);
            }
        }
    }

    @Test
    public void winLineLength_GreaterThanBoardLength() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Win Line Length cannot be greater than Board Length or there would be no way to win!");
        new Game(BOARD_LENGTH, BOARD_LENGTH + 1, players[0], players[1], players[1]);
    }

    @Test
    public void boardLength_TooSmall() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Board Length must be greater than 3 or the game would be too easy!");
        new Game(TOO_SMALL_BOARD_LENGTH, TOO_SMALL_BOARD_LENGTH, players[0], players[1], players[1]);
    }

    @Test
    public void winLineLength_TooSmall() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Win Line Length must be greater than 3 or the game would be too easy!");
        new Game(BOARD_LENGTH, TOO_SMALL_WIN_LINE_LENGTH, players[0], players[1], players[1]);
    }

    @Test
    public void players_TooFew() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Each TicTacToe game must have exactly 2 players!");
        new Game(BOARD_LENGTH, WIN_LINE_LENGTH, players1[0], players1[0], players1[0]);
    }

    @Test
    public void players_TooMany() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Each TicTacToe game must have exactly 2 players!");
        new Game(BOARD_LENGTH, WIN_LINE_LENGTH, players3[0], players3[1], players3[1]);
    }

    @Test
    public void firstPlayer_Outsider() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("First Player and Player X must be players actually playing the game!");
        new Game(BOARD_LENGTH, WIN_LINE_LENGTH, players[0], outsider, players[0]);
    }

    @Test
    public void playerX_Outsider() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("First Player and Player X must be players actually playing the game!");
        new Game(BOARD_LENGTH, WIN_LINE_LENGTH, players[0], players[0], outsider);
    }
}

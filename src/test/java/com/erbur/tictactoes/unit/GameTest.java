package com.erbur.tictactoes.unit;

import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.model.enums.Token;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class GameTest {
    private int BOARD_LENGTH = 10, WIN_LINE_LENGTH = 10;
    private int TOO_SMALL_BOARD_LENGTH = 2, TOO_SMALL_WIN_LINE_LENGTH = 2;

    private PlayerEntity[] players = new PlayerEntity[]{new PlayerEntity("Frank"), new PlayerEntity("Erik")};
    private PlayerEntity[] players1 = new PlayerEntity[]{new PlayerEntity("Frank")};
    private PlayerEntity outsider = new PlayerEntity("Frederyk");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void newGame_ValidStatistics() {
        GameEntity game;

        game = new GameEntity(BOARD_LENGTH, WIN_LINE_LENGTH, players[0], players[1], players[1]);

        assertThat(game.getMoveCount()).isEqualTo(0);
    }

    @Test
    public void newGame_ValidSizes() {
        GameEntity game = new GameEntity(BOARD_LENGTH, WIN_LINE_LENGTH, players[0], players[1], players[1]);

        assertThat(game.getBoardLength()).isEqualTo(BOARD_LENGTH);
        assertThat(game.getWinLineLength()).isEqualTo(WIN_LINE_LENGTH);

        assertThat(game.boardGet().getSize().getWidth()).isEqualTo(BOARD_LENGTH);
        assertThat(game.boardGet().getSize().getHeight()).isEqualTo(BOARD_LENGTH);

        assertThat(game.boardGet().getFields().length).isEqualTo(BOARD_LENGTH);
        assertThat(game.boardGet().getFields()[0].length).isEqualTo(BOARD_LENGTH);
    }

    @Test
    public void winLineLength_GreaterThanBoardLength() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Win Line Length cannot be greater than BoardEntity Length or there would be no way to win!");
        new GameEntity(BOARD_LENGTH, BOARD_LENGTH + 1, players[0], players[1], players[1]);
    }

    @Test
    public void boardLength_TooSmall() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("BoardEntity Length must be greater than 3 or the game would be too easy!");
        new GameEntity(TOO_SMALL_BOARD_LENGTH, TOO_SMALL_BOARD_LENGTH, players[0], players[1], players[1]);
    }

    @Test
    public void winLineLength_TooSmall() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Win Line Length must be greater than 3 or the game would be too easy!");
        new GameEntity(BOARD_LENGTH, TOO_SMALL_WIN_LINE_LENGTH, players[0], players[1], players[1]);
    }

    @Test
    public void players_TooFew() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("First PlayerEntity and Second PlayerEntity must be different!");
        new GameEntity(BOARD_LENGTH, WIN_LINE_LENGTH, players1[0], players1[0], players1[0]);
    }

    @Test
    public void player_Outsider() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("PlayerEntity X must be First or Second PlayerEntity!");
        new GameEntity(BOARD_LENGTH, WIN_LINE_LENGTH, players[0], outsider, players[1]);
    }
}

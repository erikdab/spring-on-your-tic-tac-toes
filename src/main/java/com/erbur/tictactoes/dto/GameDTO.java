package com.erbur.tictactoes.dto;

import com.erbur.tictactoes.model.Game;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class GameDTO {
    private String player1Name;
    private char player1Token;
    private String player2Name;
    private char player2Token;
    private String currentPlayer;

    private int boardLength;
    private int winLineLength;

    public GameDTO(Game game) {
        this.setPlayer1Name(game.getFirstPlayer().getName());
        this.setPlayer2Name(game.getSecondPlayer().getName());
        this.setCurrentPlayer(game.getCurrentPlayer().getName());
        this.setPlayer1Token(game.getFirstPlayerToken().toChar());
        this.setPlayer2Token(game.getSecondPlayerToken().toChar());
        this.setBoardLength(game.getBoardLength());
        this.setWinLineLength(game.getWinLineLength());
    }
}

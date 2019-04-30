package com.erbur.tictactoes.model.dto;

import com.erbur.tictactoes.model.Game;
import com.erbur.tictactoes.model.enums.Token;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class GameDTO {
    private Long firstPlayerId;
    private Token firstPlayerToken;
    private Long secondPlayerId;
    private Token secondPlayerToken;

    private int boardLength;
    private int winLineLength;

    public GameDTO(Game game) {
        this.setFirstPlayerId(game.getFirstPlayer().getId());
        this.setSecondPlayerId(game.getSecondPlayer().getId());
        this.setFirstPlayerToken(game.getFirstPlayerToken());
        this.setSecondPlayerToken(game.getSecondPlayerToken());
        this.setBoardLength(game.getBoardLength());
        this.setWinLineLength(game.getWinLineLength());
    }
}

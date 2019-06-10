package com.erbur.tictactoes.model.dto;

import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.enums.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameDTO {
    private Long firstPlayerId;
    private Token firstPlayerToken;
    private Long secondPlayerId;
    private Token secondPlayerToken;

    private int boardLength;
    private int winLineLength;

    public GameDTO(GameEntity game) {
        this.setFirstPlayerId(game.getFirstPlayer().getId());
        this.setSecondPlayerId(game.getSecondPlayer().getId());
        this.setFirstPlayerToken(game.getFirstPlayerToken());
        this.setSecondPlayerToken(game.getSecondPlayerToken());
        this.setBoardLength(game.getBoardLength());
        this.setWinLineLength(game.getWinLineLength());
    }
}

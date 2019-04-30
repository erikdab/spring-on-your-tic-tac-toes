package com.erbur.tictactoes.model.dto;

import com.erbur.tictactoes.model.GameMove;
import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.enums.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameMoveDTO {
    Long gameId;
    Point position;
    Token playerToken;

    public GameMoveDTO(GameMove gameMove) {
        gameId = gameMove.getGame().getId();
        position = gameMove.getPosition();
        playerToken = gameMove.getGame().getTokenFor(gameMove.getPlayer());
    }
}

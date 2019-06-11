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
}

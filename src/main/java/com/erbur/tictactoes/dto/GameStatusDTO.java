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
public class GameStatusDTO {
    private boolean isWon;
    private boolean isDraw;
    private String playerWonName;
    private String playerWonToken;

    private int moveCount;

    private BoardDTO board;

    public GameStatusDTO(Game game) {
        this.setWon(game.isBoardWon());
        this.setDraw(game.isBoardDraw());
        if (isWon()) {
            this.setPlayerWonName(game.getWinner().getName());
            this.setPlayerWonToken(game.getWinnerToken().toString());
        }
        this.setMoveCount(game.getMoveCount());
        this.setBoard(new BoardDTO(game.getBoard()));
    }
}

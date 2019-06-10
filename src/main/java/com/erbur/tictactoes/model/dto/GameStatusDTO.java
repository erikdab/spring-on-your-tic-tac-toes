package com.erbur.tictactoes.model.dto;

import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.GameMoveEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class GameStatusDTO {
    private boolean isWon;
    private boolean isDraw;
    private String playerWonName;
    private String playerWonToken;

    private int moveCount;

    private BoardDTO board;

    private List<GameMoveDTO> gameMoves;

    public GameStatusDTO(GameEntity game) {
        this.setWon(game.isBoardWon());
        this.setDraw(game.isBoardDraw());
        if (isWon()) {
            this.setPlayerWonName(game.getWinner().getName());
            this.setPlayerWonToken(game.getWinnerToken().toString());
        }
        this.setMoveCount(game.getMoveCount());
        this.setBoard(new BoardDTO(game.boardGet()));
        List<GameMoveDTO> gameMoveDTOS = new ArrayList<>();
        for (GameMoveEntity gameMove : game.getGameMoves()) {
            gameMoveDTOS.add(new GameMoveDTO(gameMove));
        }
        this.setGameMoves(gameMoveDTOS);
    }
}

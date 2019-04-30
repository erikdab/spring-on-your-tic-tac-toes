package com.erbur.tictactoes.model.dto;

import com.erbur.tictactoes.model.Game;
import com.erbur.tictactoes.model.GameMove;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    private List<GameMoveDTO> gameMoves;

    public GameStatusDTO(Game game) {
        this.setWon(game.isBoardWon());
        this.setDraw(game.isBoardDraw());
        if (isWon()) {
            this.setPlayerWonName(game.getWinner().getName());
            this.setPlayerWonToken(game.getWinnerToken().toString());
        }
        this.setMoveCount(game.getMoveCount());
        this.setBoard(new BoardDTO(game.boardGet()));
        List<GameMoveDTO> gameMoveDTOS = new ArrayList<>();
        for (GameMove gameMove : game.getGameMoves()) {
            gameMoveDTOS.add(new GameMoveDTO(gameMove));
        }
        this.setGameMoves(gameMoveDTOS);
    }
}

package com.erbur.tictactoes.interfaces;

import com.erbur.tictactoes.exceptions.GameNotFoundException;
import com.erbur.tictactoes.exceptions.PlayerNotFoundException;
import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.GameMoveEntity;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.model.enums.Token;

import java.util.List;

public interface GameServiceInterface {
    GameEntity newGame2(Long firstPlayerId, Long secondPlayerId, Token firstPlayerToken, int boardLength, int winLineLength) throws PlayerNotFoundException;

    void newGame(int boardLength, int winLineLength);

    void setGame(GameEntity game);

    void setGame(Long id) throws GameNotFoundException;

    void makeMove(Point point, PlayerEntity player);

    GameEntity getGame();

    List<GameEntity> findAll();

    GameEntity findOne(Long id) throws GameNotFoundException;

    void delete(Long id);

    GameMoveEntity makeMove(Point position);
}

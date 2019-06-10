package com.erbur.tictactoes.interfaces;

import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.PlayerEntity;

public interface GameInterface {
    void newGame(int boardLength, int winLineLength);

    void setGame(GameEntity game);

    void makeMove(Point point, PlayerEntity player);

    GameEntity getGame();
}

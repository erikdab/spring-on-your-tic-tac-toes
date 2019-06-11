package com.erbur.tictactoes.interfaces;

import com.erbur.tictactoes.exceptions.GameMoveNotFoundException;
import com.erbur.tictactoes.model.entities.GameMoveEntity;

public interface GameMoveServiceInterface {
    GameMoveEntity findOne(Long id) throws GameMoveNotFoundException;

    void delete(Long id);
}

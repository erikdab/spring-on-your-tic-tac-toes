package com.erbur.tictactoes.interfaces;

import com.erbur.tictactoes.exceptions.PlayerNotFoundException;
import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.PlayerEntity;

import java.util.List;

public interface PlayerServiceInterface {
    PlayerEntity addOne(PlayerEntity playerEntity);
    PlayerEntity findOne(Long id) throws PlayerNotFoundException;

    void delete(Long id);

    List<PlayerEntity> findAll();
}

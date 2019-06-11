package com.erbur.tictactoes.service;

import com.erbur.tictactoes.exceptions.GameMoveNotFoundException;
import com.erbur.tictactoes.interfaces.GameMoveServiceInterface;
import com.erbur.tictactoes.model.entities.GameMoveEntity;
import com.erbur.tictactoes.repository.GameMoveRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameMoveService implements GameMoveServiceInterface {

    private GameMoveRepository gameMoveRepository;

    public GameMoveService(GameMoveRepository gameMoveRepository) {
        this.gameMoveRepository = gameMoveRepository;
    }

    @Override
    public GameMoveEntity findOne(Long id) throws GameMoveNotFoundException {
        Optional<GameMoveEntity> gameMove = gameMoveRepository.findById(id);

        if (!gameMove.isPresent())
            throw new GameMoveNotFoundException("id-" + id);

        return gameMove.get();
    }

    @Override
    public void delete(Long id) {
        gameMoveRepository.deleteById(id);
    }
}

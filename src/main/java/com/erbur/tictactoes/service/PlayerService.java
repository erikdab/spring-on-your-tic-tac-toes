package com.erbur.tictactoes.service;

import com.erbur.tictactoes.exceptions.PlayerNotFoundException;
import com.erbur.tictactoes.interfaces.PlayerServiceInterface;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService implements PlayerServiceInterface {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public PlayerEntity addOne(PlayerEntity playerEntity) {
        return playerRepository.save(playerEntity);
    }

    @Override
    public PlayerEntity findOne(Long id) throws PlayerNotFoundException {
        Optional<PlayerEntity> player = playerRepository.findById(id);

        if (!player.isPresent())
            throw new PlayerNotFoundException("id-" + id);

        return player.get();
    }

    @Override
    public void delete(Long id) {
        playerRepository.existsById(id);
    }

    @Override
    public List<PlayerEntity> findAll() {
        return playerRepository.findAll();
    }
}

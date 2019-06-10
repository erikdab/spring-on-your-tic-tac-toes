package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.model.entities.PlayerEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.erbur.tictactoes.repository.PlayerRepository;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class PlayerRestController {
    private final PlayerRepository playerRepository;

    public PlayerRestController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping("/players")
    public ResponseEntity<Object> createPlayer(@RequestBody PlayerEntity player) {
        PlayerEntity savedPlayer = playerRepository.save(player);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPlayer.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/players")
    public List<PlayerEntity> retrieveAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/players/{id}")
    public PlayerEntity retrievePlayer(@PathVariable long id) throws PlayerNotFoundException {
        Optional<PlayerEntity> player = playerRepository.findById(id);

        if (!player.isPresent())
            throw new PlayerNotFoundException("id-" + id);

        return player.get();
    }

    static class PlayerNotFoundException extends Exception {
        public PlayerNotFoundException(String s) {
            super(s);
        }
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<Object> updatePlayer(@RequestBody PlayerEntity player, @PathVariable long id) {

        Optional<PlayerEntity> playerOptional = playerRepository.findById(id);

        if (!playerOptional.isPresent())
            return ResponseEntity.notFound().build();

        player.setId(id);

        playerRepository.save(player);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable long id) {
        playerRepository.deleteById(id);
    }

//    @GetMapping("/player-stats/{id}")
//    public void getPlayerStats() {
//
//    }
}

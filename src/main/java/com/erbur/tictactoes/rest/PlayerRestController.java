package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.model.dto.PlayerDTO;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.repository.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PlayerRestController {
    private final PlayerRepository playerRepository;

    public PlayerRestController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping("/players")
    public ResponseEntity<Object> createPlayer(@RequestBody PlayerDTO playerDTO) {
        PlayerEntity player = new PlayerEntity();
        player.setName(playerDTO.getName());

        PlayerEntity playerSaved = playerRepository.save(player);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(playerSaved.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/players")
    public List<PlayerDTO> retrieveAllPlayers() {
        return playerRepository.findAll().stream().map(PlayerDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/players/{id}")
    public PlayerDTO retrievePlayer(@PathVariable long id) throws PlayerNotFoundException {
        Optional<PlayerEntity> player = playerRepository.findById(id);

        if (!player.isPresent())
            throw new PlayerNotFoundException("id-" + id);

        return new PlayerDTO(player.get());
    }

    @PutMapping("/players/{id}")
    public ResponseEntity<Object> updatePlayer(@RequestBody PlayerDTO playerDTO, @PathVariable long id) {

        Optional<PlayerEntity> playerOptional = playerRepository.findById(id);

        if (!playerOptional.isPresent())
            return ResponseEntity.notFound().build();

        PlayerEntity player = playerOptional.get();

        player.setName(playerDTO.getName());

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

    static class PlayerNotFoundException extends Exception {
        public PlayerNotFoundException(String s) {
            super(s);
        }
    }
}

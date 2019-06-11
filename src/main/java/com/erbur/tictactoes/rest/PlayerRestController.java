package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.exceptions.PlayerNotFoundException;
import com.erbur.tictactoes.interfaces.PlayerServiceInterface;
import com.erbur.tictactoes.model.dto.PlayerDTO;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.repository.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PlayerRestController {
    private final PlayerServiceInterface playerService;

    public PlayerRestController(PlayerServiceInterface playerService, PlayerRepository playerRepository) {
        this.playerService = playerService;
    }

    @PostMapping("/players")
    public ResponseEntity<Object> createPlayer(@RequestBody PlayerDTO playerDTO) {
        PlayerEntity player = new PlayerEntity();
        player.setName(playerDTO.getName());

        PlayerEntity playerSaved = playerService.addOne(player);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(playerSaved.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/players")
    public List<PlayerDTO> retrieveAllPlayers() {
        return playerService.findAll().stream().map(PlayerDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/players/{id}")
    public PlayerDTO retrievePlayer(@PathVariable long id) throws PlayerNotFoundException {
        return new PlayerDTO(playerService.findOne(id));
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable long id) {
        playerService.delete(id);
    }
}

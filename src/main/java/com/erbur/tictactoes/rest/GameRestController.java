package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.exceptions.GameNotFoundException;
import com.erbur.tictactoes.exceptions.PlayerNotFoundException;
import com.erbur.tictactoes.interfaces.GameServiceInterface;
import com.erbur.tictactoes.model.dto.GameDTO;
import com.erbur.tictactoes.model.dto.GameStatusDTO;
import com.erbur.tictactoes.model.entities.GameEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GameRestController {
    private GameServiceInterface gameService;

    public GameRestController(GameServiceInterface gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/games")
    public ResponseEntity<Object> createGame(@RequestBody GameDTO gameDTO) throws PlayerNotFoundException {
        GameEntity savedGame = gameService.newGame2(gameDTO.getFirstPlayerId(),
                gameDTO.getSecondPlayerId(), gameDTO.getFirstPlayerToken(),
                gameDTO.getBoardLength(), gameDTO.getWinLineLength());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedGame.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/games")
    public List<GameStatusDTO> retrieveAllgames() {
        return gameService.findAll().stream().map(GameStatusDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/games/{id}")
    public GameStatusDTO retrieveGame(@PathVariable long id) throws GameNotFoundException {
        return new GameStatusDTO(gameService.findOne(id));
    }

    @DeleteMapping("/games/{id}")
    public void deleteGame(@PathVariable long id) {
        gameService.delete(id);
    }
}

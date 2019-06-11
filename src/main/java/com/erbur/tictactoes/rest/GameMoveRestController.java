package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.exceptions.GameMoveNotFoundException;
import com.erbur.tictactoes.exceptions.GameNotFoundException;
import com.erbur.tictactoes.interfaces.GameMoveServiceInterface;
import com.erbur.tictactoes.interfaces.GameServiceInterface;
import com.erbur.tictactoes.model.dto.GameMoveDTO;
import com.erbur.tictactoes.model.entities.GameMoveEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class GameMoveRestController {
    private GameServiceInterface gameService;
    private final GameMoveServiceInterface gameMoveService;

    public GameMoveRestController(GameServiceInterface gameService, GameMoveServiceInterface gameMoveService) {
        this.gameService = gameService;
        this.gameMoveService = gameMoveService;
    }

    @PostMapping("/game-moves/")
    public ResponseEntity<Object> createGameMove(@RequestBody GameMoveDTO gameMoveDTO) throws GameNotFoundException {
        gameService.setGame(gameMoveDTO.getGameId());

        GameMoveEntity savedGameMove = gameService.makeMove(gameMoveDTO.getPosition());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedGameMove.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/game-moves/{id}")
    public GameMoveDTO retrieveGameMove(@PathVariable long id) throws GameMoveNotFoundException {
        return new GameMoveDTO(gameMoveService.findOne(id));
    }


    @DeleteMapping("/game-moves/{id}")
    public void deleteGameMove(@PathVariable long id) {
        gameMoveService.delete(id);
    }
}

package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.interfaces.GameInterface;
import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.GameMoveEntity;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.model.dto.GameMoveDTO;
import com.erbur.tictactoes.repository.GameMoveRepository;
import com.erbur.tictactoes.repository.GameRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class GameMoveRestController {
    private GameInterface gameService;
    private final GameMoveRepository gameMoveRepository;
    private final GameRepository gameRepository;

    public GameMoveRestController(GameInterface gameService, GameMoveRepository gameMoveRepository, GameRepository gameRepository) {
        this.gameService = gameService;
        this.gameMoveRepository = gameMoveRepository;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/game-moves/")
    public ResponseEntity<Object> createGameMove(@RequestBody GameMoveDTO gameMoveDTO) throws GameRestController.GameNotFoundException {
        Optional<GameEntity> gameOptional = gameRepository.findById(gameMoveDTO.getGameId());
        if (! gameOptional.isPresent()) {
            throw new GameRestController.GameNotFoundException("id-" + gameMoveDTO.getGameId());
        }
        GameEntity game = gameOptional.get();
        gameService.setGame(game);

        // First Move
        int moveNumber = 0;
        PlayerEntity player = game.getFirstPlayer();
        List<GameMoveEntity> gameMoves = gameMoveRepository.findAll();
        // n-th Move
        if (! gameMoves.isEmpty()) {
            moveNumber = gameMoves.get(gameMoves.size()-1).getMoveNumber()+1;
            PlayerEntity lastPlayer = gameMoves.get(gameMoves.size()-1).getPlayer();
            player = game.getOtherPlayer(lastPlayer);
        }

        GameMoveEntity gameMove = new GameMoveEntity();
        gameMove.setGame(game);
        gameMove.setMoveNumber(moveNumber);
        gameMove.setPlayer(player);
        gameMove.setPosition(gameMoveDTO.getPosition());

        gameService.makeMove(gameMove.getPosition(), gameMove.getPlayer());

        gameRepository.save(game);
        GameMoveEntity savedGameMove = gameMoveRepository.save(gameMove);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedGameMove.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/game-moves/{id}")
    public GameMoveDTO retrieveGameMove(@PathVariable long id) throws GameMoveRestController.GameMoveNotFoundException {
        Optional<GameMoveEntity> gameMove = gameMoveRepository.findById(id);

        if (!gameMove.isPresent())
            throw new GameMoveRestController.GameMoveNotFoundException("id-" + id);

        return new GameMoveDTO(gameMove.get());
    }

    static class GameMoveNotFoundException extends Exception {
        public GameMoveNotFoundException(String s) {
            super(s);
        }
    }

    @DeleteMapping("/game-moves/{id}")
    public void deleteGameMove(@PathVariable long id) {
        gameMoveRepository.deleteById(id);
    }
}

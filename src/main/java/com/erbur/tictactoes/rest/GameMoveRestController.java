package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.interfaces.GameInterface;
import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.GameMoveEntity;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.model.dto.GameMoveDTO;
import com.erbur.tictactoes.repository.GameMoveRepository;
import com.erbur.tictactoes.repository.GameRepository;
import com.erbur.tictactoes.service.TicTacToesGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class GameMoveRestController {
    private GameInterface ticTacToesGameService;
    private final GameMoveRepository gameMoveRepository;
    private final GameRepository gameRepository;

    public GameMoveRestController(GameInterface ticTacToesGameService, GameMoveRepository gameMoveRepository, GameRepository gameRepository) {
        this.ticTacToesGameService = ticTacToesGameService;
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
        ticTacToesGameService.setGame(game);

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

        ticTacToesGameService.makeMove(gameMove.getPosition(), gameMove.getPlayer());

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

    @PutMapping("/game-moves/{id}")
    public ResponseEntity<Object> updateGameMove(@RequestBody GameMoveEntity gameMove, @PathVariable long id) {

        Optional<GameMoveEntity> studentOptional = gameMoveRepository.findById(id);

        if (!studentOptional.isPresent())
            return ResponseEntity.notFound().build();

        gameMove.setId(id);

        gameMoveRepository.save(gameMove);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/game-moves/{id}")
    public void deleteGameMove(@PathVariable long id) {
        gameMoveRepository.deleteById(id);
    }
}

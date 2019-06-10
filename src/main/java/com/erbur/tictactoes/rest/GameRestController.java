package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.interfaces.GameInterface;
import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.model.dto.GameDTO;
import com.erbur.tictactoes.model.dto.GameStatusDTO;
import com.erbur.tictactoes.model.enums.Token;
import com.erbur.tictactoes.repository.GameRepository;
import com.erbur.tictactoes.repository.PlayerRepository;
import com.erbur.tictactoes.service.TicTacToesGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class GameRestController {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameRestController(GameRepository gameRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    @PostMapping("/games")
    public ResponseEntity<Object> createGame(@RequestBody GameDTO gameDTO) throws PlayerRestController.PlayerNotFoundException {
        // boardLength, winLineLength, firstPlayer, firstPlayerToken, secondPlayer, playerX
        Optional<PlayerEntity> firstPlayer = playerRepository.findById(gameDTO.getFirstPlayerId());
        Optional<PlayerEntity> secondPlayer = playerRepository.findById(gameDTO.getSecondPlayerId());
        if (! firstPlayer.isPresent()) {
            throw new PlayerRestController.PlayerNotFoundException("First player not found id-" + gameDTO.getFirstPlayerId());
        }
        if (! secondPlayer.isPresent()) {
            throw new PlayerRestController.PlayerNotFoundException("Second player not found id-" + gameDTO.getSecondPlayerId());
        }
        PlayerEntity playerX = gameDTO.getFirstPlayerToken() == Token.X ? firstPlayer.get() : secondPlayer.get();
        GameEntity game = new GameEntity(gameDTO.getBoardLength(), gameDTO.getWinLineLength(), firstPlayer.get(), secondPlayer.get(), playerX);
        GameEntity savedGame = gameRepository.save(game);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedGame.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/games")
    public List<GameStatusDTO> retrieveAllgames() {
        List<GameEntity> games = gameRepository.findAll();
        List<GameStatusDTO> gameStatusDTOS = new ArrayList<>();
        for (GameEntity game : games) {
            gameStatusDTOS.add(new GameStatusDTO(game));
        }
        return gameStatusDTOS;
    }

    @GetMapping("/games/{id}")
    public GameStatusDTO retrieveGame(@PathVariable long id) throws GameRestController.GameNotFoundException {
        Optional<GameEntity> game = gameRepository.findById(id);

        if (!game.isPresent())
            throw new GameRestController.GameNotFoundException("id-" + id);

        return new GameStatusDTO(game.get());
    }

    static class GameNotFoundException extends Exception {
        public GameNotFoundException(String s) {
            super(s);
        }
    }

    @PutMapping("/games/{id}")
    public ResponseEntity<Object> updateGame(@RequestBody GameEntity game, @PathVariable long id) {

        Optional<GameEntity> gameOptional = gameRepository.findById(id);

        if (!gameOptional.isPresent())
            return ResponseEntity.notFound().build();

        game.setId(id);

        gameRepository.save(game);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/games/{id}")
    public void deleteGame(@PathVariable long id) {
        gameRepository.deleteById(id);
    }
}

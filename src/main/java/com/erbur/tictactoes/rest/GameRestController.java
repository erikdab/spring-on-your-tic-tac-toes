package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.model.Game;
import com.erbur.tictactoes.model.Player;
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
    private final TicTacToesGameService ticTacToesGameService;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameRestController(TicTacToesGameService ticTacToesGameService, GameRepository gameRepository, PlayerRepository playerRepository) {
        this.ticTacToesGameService = ticTacToesGameService;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    @PostMapping("/games")
    public ResponseEntity<Object> createGame(@RequestBody GameDTO gameDTO) throws PlayerRestController.PlayerNotFoundException {
        // boardLength, winLineLength, firstPlayer, firstPlayerToken, secondPlayer, playerX
        Optional<Player> firstPlayer = playerRepository.findById(gameDTO.getFirstPlayerId());
        Optional<Player> secondPlayer = playerRepository.findById(gameDTO.getSecondPlayerId());
        if (! firstPlayer.isPresent()) {
            throw new PlayerRestController.PlayerNotFoundException("First player not found id-" + gameDTO.getFirstPlayerId());
        }
        if (! secondPlayer.isPresent()) {
            throw new PlayerRestController.PlayerNotFoundException("Second player not found id-" + gameDTO.getSecondPlayerId());
        }
        Player playerX = gameDTO.getFirstPlayerToken() == Token.X ? firstPlayer.get() : secondPlayer.get();
        Game game = new Game(gameDTO.getBoardLength(), gameDTO.getWinLineLength(), firstPlayer.get(), secondPlayer.get(), playerX);
        Game savedGame = gameRepository.save(game);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedGame.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

//    @RequestMapping("/new-game")
//    public GameDTO setGame(
//            @RequestParam(value = "boardLength", defaultValue = "10") int boardLength,
//            @RequestParam(value = "winLineLength", defaultValue = "5") int winLineLength
//    ) {
//        ticTacToesGameService.setGame(boardLength, winLineLength);
//
//        return new GameDTO(ticTacToesGameService.getGame());
//    }

    @GetMapping("/games")
    public List<GameStatusDTO> retrieveAllgames() {
        List<Game> games = gameRepository.findAll();
        List<GameStatusDTO> gameStatusDTOS = new ArrayList<>();
        for (Game game : games) {
            gameStatusDTOS.add(new GameStatusDTO(game));
        }
        return gameStatusDTOS;
    }

    @GetMapping("/games/{id}")
    public GameStatusDTO retrieveGame(@PathVariable long id) throws GameRestController.GameNotFoundException {
        Optional<Game> game = gameRepository.findById(id);

        if (!game.isPresent())
            throw new GameRestController.GameNotFoundException("id-" + id);

        return new GameStatusDTO(game.get());
    }

    static class GameNotFoundException extends Throwable {
        public GameNotFoundException(String s) {
            super(s);
        }
    }

//    @RequestMapping("/status")
//    public GameStatusDTO gameStatus() throws IllegalStateException {
//        if (ticTacToesGameService.getGame() == null)
//            throw new IllegalStateException("Game not started yet! Please start a game using '/new-game' first.");
//
//        return new GameStatusDTO(ticTacToesGameService.getGame());
//    }

    @PutMapping("/games/{id}")
    public ResponseEntity<Object> updateGame(@RequestBody Game game, @PathVariable long id) {

        Optional<Game> gameOptional = gameRepository.findById(id);

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

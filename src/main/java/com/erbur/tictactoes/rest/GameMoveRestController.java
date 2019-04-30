package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.model.Game;
import com.erbur.tictactoes.model.GameMove;
import com.erbur.tictactoes.model.Player;
import com.erbur.tictactoes.model.dto.GameMoveDTO;
import com.erbur.tictactoes.repository.GameMoveRepository;
import com.erbur.tictactoes.repository.GameRepository;
import com.erbur.tictactoes.service.TicTacToesGameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class GameMoveRestController {
    private TicTacToesGameService ticTacToesGameService;
    private final GameMoveRepository gameMoveRepository;
    private final GameRepository gameRepository;

    public GameMoveRestController(TicTacToesGameService ticTacToesGameService, GameMoveRepository gameMoveRepository, GameRepository gameRepository) {
        this.ticTacToesGameService = ticTacToesGameService;
        this.gameMoveRepository = gameMoveRepository;
        this.gameRepository = gameRepository;
    }

    @PostMapping("/game-moves/")
    public ResponseEntity<Object> createGameMove(@RequestBody GameMoveDTO gameMoveDTO) throws GameRestController.GameNotFoundException {
        Optional<Game> gameOptional = gameRepository.findById(gameMoveDTO.getGameId());
        if (! gameOptional.isPresent()) {
            throw new GameRestController.GameNotFoundException("id-" + gameMoveDTO.getGameId());
        }
        Game game = gameOptional.get();
        ticTacToesGameService.setGame(game);

        // First Move
        int moveNumber = 0;
        Player player = game.getFirstPlayer();
        List<GameMove> gameMoves = gameMoveRepository.findAll();
        // n-th Move
        if (! gameMoves.isEmpty()) {
            moveNumber = gameMoves.get(gameMoves.size()-1).getMoveNumber()+1;
            Player lastPlayer = gameMoves.get(gameMoves.size()-1).getPlayer();
            player = game.getOtherPlayer(lastPlayer);
        }

        GameMove gameMove = new GameMove();
        gameMove.setGame(game);
        gameMove.setMoveNumber(moveNumber);
        gameMove.setPlayer(player);
        gameMove.setPosition(gameMoveDTO.getPosition());

        ticTacToesGameService.makeMove(gameMove.getPosition(), gameMove.getPlayer());

        gameRepository.save(game);
        GameMove savedGameMove = gameMoveRepository.save(gameMove);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedGameMove.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

//    @PutMapping("/games/{id}/move")
//    public GameStatusDTO makeMove(@RequestBody GameMoveDto game, @PathVariable long id) throws IllegalStateException {
//        if (ticTacToesGameService.getGame() == null)
//            throw new IllegalStateException("Game not started yet! Please start a game using '/new-game' first.");
//
//        Token token = Token.fromChar(tokenChar);
//        Game game = ticTacToesGameService.getGame();
//        ticTacToesGameService.makeMove(new Point(x, y), game.getPlayerFor(token));
//
//        return new GameStatusDTO(ticTacToesGameService.getGame());
//    }
//
//    @GetMapping("/game-moves/{gameId}")
//    public List<GameMove> retrieveAllGameMoves(@PathVariable long gameId) throws GameRestController.GameNotFoundException {
//        Optional<Game> game = gameRepository.findById(gameId);
//        if (! game.isPresent()) {
//            throw new GameRestController.GameNotFoundException("id-" + gameId);
//        }
//        return game.get().getGameMoves();
//    }

    @GetMapping("/game-moves/{id}")
    public GameMoveDTO retrieveGameMove(@PathVariable long id) throws GameMoveRestController.GameMoveNotFoundException {
        Optional<GameMove> gameMove = gameMoveRepository.findById(id);

        if (!gameMove.isPresent())
            throw new GameMoveRestController.GameMoveNotFoundException("id-" + id);

        return new GameMoveDTO(gameMove.get());
    }

    static class GameMoveNotFoundException extends Throwable {
        public GameMoveNotFoundException(String s) {
            super(s);
        }
    }

    @PutMapping("/game-moves/{id}")
    public ResponseEntity<Object> updateGameMove(@RequestBody GameMove gameMove, @PathVariable long id) {

        Optional<GameMove> studentOptional = gameMoveRepository.findById(id);

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

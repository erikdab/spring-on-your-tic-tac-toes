package com.erbur.tictactoes.api;

import com.erbur.tictactoes.dto.GameDTO;
import com.erbur.tictactoes.dto.GameStatusDTO;
import com.erbur.tictactoes.logic.TicTacToesGame;
import com.erbur.tictactoes.model.Game;
import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.Token;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicTacToesApi {
    private TicTacToesGame ticTacToesGame = new TicTacToesGame();

    @RequestMapping("/new-game")
    public GameDTO newGame(
            @RequestParam(value = "boardLength", defaultValue = "10") int boardLength,
            @RequestParam(value = "winLineLength", defaultValue = "5") int winLineLength
    ) {
        ticTacToesGame.newGame(boardLength, winLineLength);

        return new GameDTO(ticTacToesGame.getGame());
    }

    @RequestMapping("/make-move")
    public GameStatusDTO makeMove(
            @RequestParam(value = "x") int x,
            @RequestParam(value = "y") int y,
            @RequestParam(value = "player") char tokenChar
    ) throws IllegalStateException {
        if (ticTacToesGame.getGame() == null)
            throw new IllegalStateException("Game not started yet! Please start a game using '/new-game' first.");

        Token token = Token.fromChar(tokenChar);
        Game game = ticTacToesGame.getGame();
        ticTacToesGame.makeMove(new Point(x, y), game.getPlayerFor(token));

        return new GameStatusDTO(ticTacToesGame.getGame());
    }

    @RequestMapping("/status")
    public GameStatusDTO gameStatus() throws IllegalStateException {
        if (ticTacToesGame.getGame() == null)
            throw new IllegalStateException("Game not started yet! Please start a game using '/new-game' first.");

        return new GameStatusDTO(ticTacToesGame.getGame());
    }
}

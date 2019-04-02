package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.model.dto.GameDTO;
import com.erbur.tictactoes.model.dto.GameStatusDTO;
import com.erbur.tictactoes.service.TicTacToesGameService;
import com.erbur.tictactoes.model.Game;
import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.enums.Token;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicTacToesController {
    private TicTacToesGameService ticTacToesGameService = new TicTacToesGameService();

    @RequestMapping("/new-game")
    public GameDTO newGame(
            @RequestParam(value = "boardLength", defaultValue = "10") int boardLength,
            @RequestParam(value = "winLineLength", defaultValue = "5") int winLineLength
    ) {
        ticTacToesGameService.newGame(boardLength, winLineLength);

        return new GameDTO(ticTacToesGameService.getGame());
    }

    @RequestMapping("/make-move")
    public GameStatusDTO makeMove(
            @RequestParam(value = "x") int x,
            @RequestParam(value = "y") int y,
            @RequestParam(value = "player") char tokenChar
    ) throws IllegalStateException {
        if (ticTacToesGameService.getGame() == null)
            throw new IllegalStateException("Game not started yet! Please start a game using '/new-game' first.");

        Token token = Token.fromChar(tokenChar);
        Game game = ticTacToesGameService.getGame();
        ticTacToesGameService.makeMove(new Point(x, y), game.getPlayerFor(token));

        return new GameStatusDTO(ticTacToesGameService.getGame());
    }

    @RequestMapping("/status")
    public GameStatusDTO gameStatus() throws IllegalStateException {
        if (ticTacToesGameService.getGame() == null)
            throw new IllegalStateException("Game not started yet! Please start a game using '/new-game' first.");

        return new GameStatusDTO(ticTacToesGameService.getGame());
    }
}

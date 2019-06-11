package com.erbur.tictactoes.service;

import com.erbur.tictactoes.exceptions.GameNotFoundException;
import com.erbur.tictactoes.exceptions.PlayerNotFoundException;
import com.erbur.tictactoes.interfaces.GameServiceInterface;
import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.Size;
import com.erbur.tictactoes.model.entities.BoardEntity;
import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.GameMoveEntity;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.model.enums.Token;
import com.erbur.tictactoes.repository.GameMoveRepository;
import com.erbur.tictactoes.repository.GameRepository;
import com.erbur.tictactoes.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameService implements GameServiceInterface {
    private final GameRepository gameRepository;
    private final GameMoveRepository gameMoveRepository;
    private final PlayerRepository playerRepository;
    private GameEntity game;

    // Other things to consider: history, maybe put check victory condition in BoardEntity (maybe not)

    public GameService(GameRepository gameRepository, GameMoveRepository gameMoveRepository, PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.gameMoveRepository = gameMoveRepository;
        this.playerRepository = playerRepository;
    }

    public GameEntity newGame2(Long firstPlayerId, Long secondPlayerId, Token firstPlayerToken, int boardLength, int winLineLength) throws PlayerNotFoundException {
        Optional<PlayerEntity> firstPlayer = playerRepository.findById(firstPlayerId);
        Optional<PlayerEntity> secondPlayer = playerRepository.findById(secondPlayerId);
        if (! firstPlayer.isPresent()) {
            throw new PlayerNotFoundException("First player not found id-" + firstPlayerId);
        }
        if (! secondPlayer.isPresent()) {
            throw new PlayerNotFoundException("Second player not found id-" + secondPlayerId);
        }
        PlayerEntity playerX = firstPlayerToken == Token.X ? firstPlayer.get() : secondPlayer.get();
        game = new GameEntity(boardLength, winLineLength, firstPlayer.get(), secondPlayer.get(), playerX);
        return gameRepository.save(game);
    }

    public List<GameEntity> findAll() {
        return gameRepository.findAll();
    }

    public GameEntity findOne(Long id) throws GameNotFoundException {
        Optional<GameEntity> game = gameRepository.findById(id);

        if (!game.isPresent())
            throw new GameNotFoundException("id-" + id);

        this.game = game.get();
        return this.game;
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
    }

    public GameMoveEntity makeMove(Point position) {
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
        gameMove.setPosition(position);

        game.addMove(position, player);

        checkVictoryConditionAfterMove(position, player);

        gameRepository.save(game);

        return gameMoveRepository.save(gameMove);
    }

    // Pass more params in new game.
    public void newGame(int boardLength, int winLineLength) {
        // These could be in the database
        // They can have statistics of how many victories / etc.
        PlayerEntity[] players = new PlayerEntity[]{new PlayerEntity("Frank"), new PlayerEntity("Erik")};

        game = new GameEntity(boardLength, winLineLength, players[0], players[1], players[1]);
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public void setGame(Long gameId) throws GameNotFoundException {
        Optional<GameEntity> gameOptional = gameRepository.findById(gameId);
        if (! gameOptional.isPresent()) {
            throw new GameNotFoundException("id-" + gameId);
        }
        GameEntity game = gameOptional.get();
        setGame(game);
    }

    public void makeMove(Point point, PlayerEntity player) {
        game.addMove(point, player);

        checkVictoryConditionAfterMove(point, player);
    }

    public GameEntity getGame() {
        return game;
    }

    // horizontal or vertical
    private boolean checkWinHorizontal(Point move, Token token, boolean horizontal) {
        int x = move.getX();
        int y = move.getY();
        BoardEntity board = game.boardGet();
        int boardLength = game.getBoardLength();
        int winLineLength = game.getWinLineLength();

        int count = 0;
        for (int i = 0; i < boardLength; i++) {
            Token currentToken = horizontal ? board.getField(i, y) : board.getField(x, i);
            if (currentToken == token) count++;
            else count = 0;

            if (count >= winLineLength) break;
        }
        return count >= winLineLength;
    }

    private boolean legalDiagonal(Point move, boolean diagonal) {
        int x = move.getX();
        int y = move.getY();
        int boardLength = game.getBoardLength();
        int winLineLength = game.getWinLineLength();
        int leftoverSpace = boardLength - winLineLength;
        int spacePast = boardLength + leftoverSpace;

        if (diagonal) return x == y || x - y <= leftoverSpace && y - x <= leftoverSpace;
        else return x + y >= winLineLength || x + y <= spacePast;
    }

    // diagonal or antidiagonal
    private boolean checkWinDiagonal(Point move, Token token, boolean diagonal) {
        if (!legalDiagonal(move, diagonal)) return false;

        BoardEntity board = game.boardGet();
        Size boardSize = board.getSize();
        int winLineLength = game.getWinLineLength();

        // Find start point on x or y axis
        Point location = new Point(move);
        while (!location.atZeroAxis()) {
            location.moveUp();
            if (diagonal) location.moveLeft();
            else location.moveRight();
        }
        // Travel through entire diagonal and see if it is completed.
        int count = 0;
        while (!location.outside(boardSize)) {
            if (board.getField(location) == token)
                count++;
            else count = 0;

            if (count >= winLineLength) break;

            location.moveDown();
            if (diagonal) location.moveRight();
            else location.moveLeft();
        }
        return count >= winLineLength;
    }

    private boolean checkDraw() {
        int boardLength = game.getBoardLength();

        return game.getMoveCount() >= (Math.pow(boardLength, 2) - 1);
    }

    // I could easily make this work for checking victory condition whenever,
    // not just after a move (but rather checking if the board is won simply, and by whom).
    private void checkVictoryConditionAfterMove(Point move, PlayerEntity player) {
        Token token = game.getTokenFor(player);

        // check column - horizontal
        if (checkWinHorizontal(move, token, true)) {
            game.setWinner(player);
            return;
        }

        // check row - vertical
        if (checkWinHorizontal(move, token, false)) {
            game.setWinner(player);
            return;
        }

        // check diagonal, but only if the move was on a legal diagonal
        if (checkWinDiagonal(move, token, true)) {
            game.setWinner(player);
            return;
        }

        // check anti-diagonal, but only if the move was on a legal anti-diagonal
        if (checkWinDiagonal(move, token, false)) {
            game.setWinner(player);
            return;
        }

        // check draw - if all moves were used up and a win did not occur
        if (checkDraw()) game.setBoardDraw(true);
    }
}

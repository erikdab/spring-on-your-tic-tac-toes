package com.erbur.tictactoes.model.entities;

import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.enums.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "game")
public class GameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private transient BoardEntity board = null;

    @Transient
    public BoardEntity boardGet() {
        if (board == null) setupBoard();
        return board;
    }

    private void setupBoard() {
        board = new BoardEntity(boardLength, boardLength);
        for (GameMoveEntity gameMove : gameMoves) {
            addMove(gameMove.position, gameMove.player);
        }
    }

    // Should these be placed in board or maybe create a GameEntity class (only data - service here)
    private boolean boardWon = false;
    private boolean boardDraw = false;
    private transient int moveCount = 0;
    private Token winnerToken = null;

    @ManyToOne
    @JoinColumn
    private PlayerEntity firstPlayer;
    private Token firstPlayerToken;
    @ManyToOne
    @JoinColumn
    private PlayerEntity secondPlayer;
    private Token secondPlayerToken;

    @NotNull
    private int boardLength;
    @NotNull
    private int winLineLength;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameMoveEntity> gameMoves;

    // Another constructor with board?
    public GameEntity(int boardLength, int winLineLength, PlayerEntity firstPlayer, PlayerEntity secondPlayer, PlayerEntity playerX) {
        if (boardLength < 3)
            throw new IllegalArgumentException("BoardEntity Length must be greater than 3 or the game would be too easy!");

        if (winLineLength > boardLength)
            throw new IllegalArgumentException("Win Line Length cannot be greater than BoardEntity Length or there would be no way to win!");
        else if (winLineLength < 3)
            throw new IllegalArgumentException("Win Line Length must be greater than 3 or the game would be too easy!");

        this.boardLength = boardLength;
        this.board = new BoardEntity(boardLength, boardLength);
        this.winLineLength = winLineLength;

        if (firstPlayer == secondPlayer) {
            throw new IllegalArgumentException("First PlayerEntity and Second PlayerEntity must be different!");
        }
        if (firstPlayer != playerX && secondPlayer != playerX) {
            throw new IllegalArgumentException("PlayerEntity X must be First or Second PlayerEntity!");
        }

        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;

        firstPlayerToken = playerX == firstPlayer ? Token.X : Token.O;
        secondPlayerToken = firstPlayerToken == Token.X ? Token.O : Token.X;
    }

    public void setGameMoves(List<GameMoveEntity> gameMoves) {
        this.gameMoves = gameMoves;
        setupBoard();
    }

    public void setBoardFields(Token[][] fields) {
        board.setFields(fields);
        moveCount = 0;
        for (Token[] row : fields) {
            for (Token cell : row) {
                if (cell != Token.Blank) moveCount++;
            }
        }
    }

    public void setWinner(PlayerEntity player) {
        setWinnerToken(getTokenFor(player));
    }

    public void setWinnerToken(Token token) {
        this.winnerToken = token;
        this.boardWon = true;
    }

    public PlayerEntity getWinner() {
        if(! this.boardWon) return null;
        return getPlayerFor(winnerToken);
    }

    public PlayerEntity getOtherPlayer(PlayerEntity player) {
        return firstPlayer == player ? firstPlayer : secondPlayer;
    }

    public void addMove(Point move, PlayerEntity player) {
        if (isBoardDraw() || isBoardWon())
            throw new IllegalArgumentException("This game is completed, start a new game to continue playing.");
        PlayerEntity currentPlayer = getCurrentPlayer();
        if (player != currentPlayer) throw new IllegalArgumentException(
                String.format("PlayerEntity %s (%s) cannot play now since it is player %s's (%s) turn.",
                        player, getTokenFor(player).toChar(), currentPlayer, getTokenFor(currentPlayer)));
        if (move.outside(boardGet().getSize())) throw new IllegalArgumentException("Move is outside box!");
        boardGet().setField(move, getTokenFor(player));
        moveCount++;
    }

    public Token getTokenFor(PlayerEntity player) {
        return player == firstPlayer ? firstPlayerToken : secondPlayerToken;
    }

    public PlayerEntity getPlayerFor(Token token) {
        return firstPlayerToken == token ? firstPlayer : secondPlayer;
    }

    public PlayerEntity getCurrentPlayer() {
        return moveCount % 2 == 0 ? firstPlayer : secondPlayer;
    }

    public Token getFirstPlayerToken() {
        return getTokenFor(firstPlayer);
    }

    public Token getSecondPlayerToken() {
        return getTokenFor(getSecondPlayer());
    }
}

package com.erbur.tictactoes.model;

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
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private transient Board board = null;

    @Transient
    public Board boardGet() {
        if (board == null) setupBoard();
        return board;
    }

    private void setupBoard() {
        board = new Board(boardLength, boardLength);
        for (GameMove gameMove : gameMoves) {
            addMove(gameMove.position, gameMove.player);
        }
    }

    // Should these be placed in board or maybe create a Game class (only data - service here)
    private boolean boardWon = false;
    private boolean boardDraw = false;
    private transient int moveCount = 0;
    private Token winnerToken = null;

    @ManyToOne
    @JoinColumn
    private Player firstPlayer;
    private Token firstPlayerToken;
    @ManyToOne
    @JoinColumn
    private Player secondPlayer;
    private Token secondPlayerToken;

    @NotNull
    private int boardLength;
    @NotNull
    private int winLineLength;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameMove> gameMoves;

    // Another constructor with board?
    public Game(int boardLength, int winLineLength, Player firstPlayer, Player secondPlayer, Player playerX) {
        if (boardLength < 3)
            throw new IllegalArgumentException("Board Length must be greater than 3 or the game would be too easy!");

        if (winLineLength > boardLength)
            throw new IllegalArgumentException("Win Line Length cannot be greater than Board Length or there would be no way to win!");
        else if (winLineLength < 3)
            throw new IllegalArgumentException("Win Line Length must be greater than 3 or the game would be too easy!");

        this.boardLength = boardLength;
        this.board = new Board(boardLength, boardLength);
        this.winLineLength = winLineLength;

        if (firstPlayer == secondPlayer) {
            throw new IllegalArgumentException("First Player and Second Player must be different!");
        }
        if (firstPlayer != playerX && secondPlayer != playerX) {
            throw new IllegalArgumentException("Player X must be First or Second Player!");
        }

        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;

        firstPlayerToken = playerX == firstPlayer ? Token.X : Token.O;
        secondPlayerToken = firstPlayerToken == Token.X ? Token.O : Token.X;
    }

    public void setGameMoves(List<GameMove> gameMoves) {
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

    public void setWinner(Player player) {
        setWinnerToken(getTokenFor(player));
    }

    public void setWinnerToken(Token token) {
        this.winnerToken = token;
        this.boardWon = true;
    }

    public Player getWinner() {
        if(! this.boardWon) return null;
        return getPlayerFor(winnerToken);
    }

    public Player getOtherPlayer(Player player) {
        return firstPlayer == player ? firstPlayer : secondPlayer;
    }

    public void addMove(Point move, Player player) {
        if (isBoardDraw() || isBoardWon())
            throw new IllegalArgumentException("This game is completed, start a new game to continue playing.");
        Player currentPlayer = getCurrentPlayer();
        if (player != currentPlayer) throw new IllegalArgumentException(
                String.format("Player %s (%s) cannot play now since it is player %s's (%s) turn.",
                        player, getTokenFor(player).toChar(), currentPlayer, getTokenFor(currentPlayer)));
        if (move.outside(boardGet().getSize())) throw new IllegalArgumentException("Move is outside box!");
        boardGet().setField(move, getTokenFor(player));
        moveCount++;
    }

    public Token getTokenFor(Player player) {
        return player == firstPlayer ? firstPlayerToken : secondPlayerToken;
    }

    public Player getPlayerFor(Token token) {
        return firstPlayerToken == token ? firstPlayer : secondPlayer;
    }

    public Player getCurrentPlayer() {
        return moveCount % 2 == 0 ? firstPlayer : secondPlayer;
    }

    public Token getFirstPlayerToken() {
        return getTokenFor(firstPlayer);
    }

    public Token getSecondPlayerToken() {
        return getTokenFor(getSecondPlayer());
    }
}

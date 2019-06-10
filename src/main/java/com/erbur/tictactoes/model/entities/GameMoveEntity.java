package com.erbur.tictactoes.model.entities;

import com.erbur.tictactoes.model.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "game_move")
public class GameMoveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    int moveNumber;

    @NotNull
    Point position;

    @ManyToOne
    @JoinColumn
    PlayerEntity player;

    @ManyToOne
    @JoinColumn
    GameEntity game;
}

package com.erbur.tictactoes.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "game_move")
public class GameMove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    int moveNumber;

    @NotNull
    Point position;

    @ManyToOne
    @JoinColumn
    Player player;

    @ManyToOne
    @JoinColumn
    Game game;
}

package com.erbur.tictactoes.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Player {
    private String name;

    public Player(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}

package com.erbur.tictactoes.dto;

import com.erbur.tictactoes.model.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PlayerDTO {
    private String name;

    public PlayerDTO(Player player) {
        this.name = player.getName();
    }
}

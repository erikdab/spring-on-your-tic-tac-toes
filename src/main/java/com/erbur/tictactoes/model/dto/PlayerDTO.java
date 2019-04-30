package com.erbur.tictactoes.model.dto;

import com.erbur.tictactoes.model.Player;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerDTO {
    private String name;

    public PlayerDTO(Player player) {
        this.name = player.getName();
    }
}

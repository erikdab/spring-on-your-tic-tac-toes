package com.erbur.tictactoes.model.dto;

import com.erbur.tictactoes.model.entities.PlayerEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlayerDTO {
    private String name;

    public PlayerDTO(PlayerEntity player) {
        this.name = player.getName();
    }
}

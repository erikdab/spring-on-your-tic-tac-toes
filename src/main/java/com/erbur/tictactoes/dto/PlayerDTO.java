package com.erbur.tictactoes.dto;

import com.erbur.tictactoes.model.Player;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
public class PlayerDTO {
    private String name;

    public PlayerDTO(Player player) {
        this.name = player.getName();
    }
}

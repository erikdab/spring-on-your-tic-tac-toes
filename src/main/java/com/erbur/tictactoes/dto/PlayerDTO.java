package com.erbur.tictactoes.dto;

import com.erbur.tictactoes.model.Player;

public class PlayerDTO {
    private String name;

    public PlayerDTO() {}
    public PlayerDTO(String name) {
        this.name = name;
    }
    public PlayerDTO(Player player) {
        this.name = player.getName();
    }

    public String getName() {
        return name;
    }
}

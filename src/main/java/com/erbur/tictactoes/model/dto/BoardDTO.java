package com.erbur.tictactoes.model.dto;

import com.erbur.tictactoes.model.Board;
import com.erbur.tictactoes.model.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDTO {
    private char[][] fields;
    private Size size;

    public BoardDTO(Board board) {
        this.fields = board.getFieldsChar();
        this.size = board.getSize();
    }
}

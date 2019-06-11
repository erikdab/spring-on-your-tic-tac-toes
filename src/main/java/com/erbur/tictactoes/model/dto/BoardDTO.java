package com.erbur.tictactoes.model.dto;

import com.erbur.tictactoes.model.entities.BoardEntity;
import com.erbur.tictactoes.model.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardDTO {
    private char[][] fields;
    private Size size;

    public final char[][] getFields() {
        return this.fields.clone();
    }

    public BoardDTO(BoardEntity board) {
        this.fields = board.getFieldsChar();
        this.size = board.getSize();
    }
}

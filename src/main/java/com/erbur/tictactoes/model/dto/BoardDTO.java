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

    public void setFields(char[][] fields) {
        this.fields = new char[fields.length][fields[0].length];

        for (int x = 0; x < fields.length; x++) {
            for (int y = 0; y < fields[x].length; y++) {
                this.fields[y][x] = fields[y][x];
            }
        }
    }

    public final char[][] getFields() {
        return this.fields.clone();
    }

    public BoardDTO(Board board) {
        this.fields = board.getFieldsChar();
        this.size = board.getSize();
    }
}

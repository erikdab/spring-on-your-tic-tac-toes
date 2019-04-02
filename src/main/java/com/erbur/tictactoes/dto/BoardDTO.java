package com.erbur.tictactoes.dto;

import com.erbur.tictactoes.model.Board;
import com.erbur.tictactoes.model.Size;

public class BoardDTO {
    private char[][] fields;
    private Size size;

    public BoardDTO() {}

    public BoardDTO(char[][] fields, Size size) {
        this.setFields(fields);
        this.setSize(size);
    }

    public BoardDTO(Board board) {
        this.setFields(board.getFieldsChar());
        this.setSize(board.getSize());
    }

    public char[][] getFields() {
        return fields;
    }

    public void setFields(char[][] fields) {
        this.fields = fields;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}

package com.erbur.tictactoes.model.enums;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TokenTest {

    @Test
    public void toChar() {
        Token token = Token.O;
        assertThat(token.toChar()).isEqualTo('O');
    }
}
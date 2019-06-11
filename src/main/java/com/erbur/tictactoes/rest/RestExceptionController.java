package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.exceptions.GameMoveNotFoundException;
import com.erbur.tictactoes.exceptions.GameNotFoundException;
import com.erbur.tictactoes.exceptions.PlayerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class RestExceptionController {
    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void gameNotFoundException(Exception ex) {
        log.info("error " + ex);
    }

    @ExceptionHandler(GameMoveNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void gameMoveNotFoundException(Exception ex) {
        log.info("error " + ex);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void playerNotFoundException(Exception ex) {
        log.info("error " + ex);
    }
}

package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.TicTacToesApplication;
import com.erbur.tictactoes.interfaces.GameServiceInterface;
import com.erbur.tictactoes.model.Point;
import com.erbur.tictactoes.model.dto.GameDTO;
import com.erbur.tictactoes.model.dto.GameMoveDTO;
import com.erbur.tictactoes.model.dto.GameStatusDTO;
import com.erbur.tictactoes.model.entities.GameEntity;
import com.erbur.tictactoes.model.entities.PlayerEntity;
import com.erbur.tictactoes.model.enums.Token;
import com.erbur.tictactoes.repository.GameMoveRepository;
import com.erbur.tictactoes.repository.GameRepository;
import com.erbur.tictactoes.repository.PlayerRepository;
import com.erbur.tictactoes.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicTacToesApplication.class)
@ActiveProfiles("test")
public class GameRestControllerTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private GameServiceInterface gameService;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();


    @Before
    public void setupTests() {
//        GameEntity gameEntity = new GameEntity();
//
//        Optional<GameEntity> optGameEntity = Optional.of(gameEntity);
//
//        GameRepository gameRepository = Mockito.mock(GameRepository.class);
//        Mockito.when(gameRepository.findById(1L)).thenReturn(optGameEntity);
//
//        GameMoveRepository gameMoveRepository = Mockito.mock(GameMoveRepository.class);
//
//        PlayerEntity player1 = new PlayerEntity();
//        player1.setName("Erik");
//
//        PlayerEntity player2 = new PlayerEntity();
//        player2.setName("Jonathan");
//
//        PlayerRepository playerRepository = Mockito.mock(PlayerRepository.class);
//
//        Mockito.when(playerRepository.findById(1L)).thenReturn(Optional.of(player1));
//        Mockito.when(playerRepository.findById(2L)).thenReturn(Optional.of(player2));
//
//        gameService = new GameService(gameRepository, gameMoveRepository, playerRepository);

        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testStartNewGame() throws Exception {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setFirstPlayerId(1L);
        gameDTO.setSecondPlayerId(2L);
        gameDTO.setBoardLength(5);
        gameDTO.setWinLineLength(3);
        gameDTO.setFirstPlayerToken(Token.X);

        mockServer.expect(ExpectedCount.once(),
                requestTo("http://localhost:8080/games"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(gameDTO)));

        restTemplate.postForObject("http://localhost:8080/games", gameDTO, GameDTO.class);

        mockServer.verify();
    }

    @Test
    public void testMakeMove() throws Exception {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setFirstPlayerId(1L);
        gameDTO.setSecondPlayerId(2L);
        gameDTO.setBoardLength(5);
        gameDTO.setWinLineLength(3);
        gameDTO.setFirstPlayerToken(Token.X);

        GameMoveDTO gameMoveDTO = new GameMoveDTO();
        gameMoveDTO.setGameId(1L);
        gameMoveDTO.setPosition(new Point(0, 1));

        mockServer.expect(ExpectedCount.once(),
                requestTo("http://localhost:8080/games"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(gameDTO)));

        restTemplate.postForObject("http://localhost:8080/games", gameDTO, GameDTO.class);

        mockServer.verify();

        mockServer.reset();

        mockServer.expect(ExpectedCount.once(),
                requestTo("http://localhost:8080/game-moves"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(gameMoveDTO)));

        restTemplate.postForObject("http://localhost:8080/game-moves", gameMoveDTO, GameMoveDTO.class);

        mockServer.verify();
    }
}

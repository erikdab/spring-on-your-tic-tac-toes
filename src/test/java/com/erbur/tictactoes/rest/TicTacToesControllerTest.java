package com.erbur.tictactoes.rest;

import com.erbur.tictactoes.TicTacToesApplication;
import com.erbur.tictactoes.model.dto.GameDTO;
import com.erbur.tictactoes.model.dto.GameStatusDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicTacToesApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TicTacToesControllerTest {
    @LocalServerPort
    private int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    UriComponentsBuilder newGameUriBuilder;
    UriComponentsBuilder makeMoveO1UriBuilder;
    UriComponentsBuilder makeMoveX1UriBuilder;

    @Before
    public void setupTests() {
        newGameUriBuilder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/new-game"))
                .queryParam("boardLength", 9)
                .queryParam("winLineLength", 4);

        makeMoveO1UriBuilder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/make-move"))
                .queryParam("x", 0)
                .queryParam("y", 1)
                .queryParam("player", 'O');

        makeMoveX1UriBuilder = UriComponentsBuilder.fromHttpUrl(createURLWithPort("/make-move"))
                .queryParam("x", 1)
                .queryParam("y", 1)
                .queryParam("player", 'X');
    }

    @Test
    public void testStartNewGame() throws Exception {
        HttpEntity<?> entity = new HttpEntity<>(null, headers);

        ResponseEntity<GameDTO> response = restTemplate.exchange(
                createURLWithPort("/new-game"), HttpMethod.GET, entity, GameDTO.class);

        GameDTO actual = response.getBody();

        // Default values
        assertThat(actual).isNotNull();
        assertThat(actual.getBoardLength()).isEqualTo(10);
        assertThat(actual.getWinLineLength()).isEqualTo(5);
    }

    @Test
    public void testStartNewGameWithParams() throws Exception {
        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<GameDTO> response = restTemplate.exchange(
                newGameUriBuilder.toUriString(), HttpMethod.GET, entity, GameDTO.class);

        GameDTO actual = response.getBody();

        assertThat(actual).isNotNull();
        assertThat(actual.getBoardLength()).isEqualTo(9);
        assertThat(actual.getWinLineLength()).isEqualTo(4);
        assertThat(actual.getCurrentPlayer()).isEqualTo(actual.getPlayer1Name());
    }

    @Test
    public void testMakeMove() throws Exception {
        // First start a new game.
        HttpEntity<?> newGameEntity = new HttpEntity<>(headers);

        ResponseEntity<GameDTO> newGameResponse = restTemplate.exchange(
                newGameUriBuilder.toUriString(), HttpMethod.GET, newGameEntity, GameDTO.class);

        GameDTO actual = newGameResponse.getBody();
        assertThat(actual).isNotNull();

        // Then make a O move
        HttpEntity<?> makeMoveOEntity = new HttpEntity<>(headers);

        ResponseEntity<GameStatusDTO> makeMoveOResponse = restTemplate.exchange(
                makeMoveO1UriBuilder.toUriString(), HttpMethod.GET, makeMoveOEntity, GameStatusDTO.class);

        GameStatusDTO makeMoveOActual = makeMoveOResponse.getBody();
        assertThat(makeMoveOActual).isNotNull();
        assertThat(makeMoveOActual.getMoveCount()).isEqualTo(1);
        assertThat(makeMoveOActual.getBoard().getFields()[1][0]).isEqualTo('O');


        // Then make a X move.
        HttpEntity<?> makeMoveXEntity = new HttpEntity<>(headers);

        ResponseEntity<GameStatusDTO> makeMoveXResponse = restTemplate.exchange(
                makeMoveX1UriBuilder.toUriString(), HttpMethod.GET, makeMoveXEntity, GameStatusDTO.class);

        GameStatusDTO makeMoveXActual = makeMoveXResponse.getBody();
        assertThat(makeMoveXActual).isNotNull();
        assertThat(makeMoveXActual.getMoveCount()).isEqualTo(2);
        assertThat(makeMoveXActual.getBoard().getFields()[1][1]).isEqualTo('X');
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}

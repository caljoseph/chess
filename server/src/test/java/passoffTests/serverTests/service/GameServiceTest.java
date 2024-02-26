package passoffTests.serverTests.service;

import model.GameRequest;
import model.GameResponse;
import model.FailureResponse;
import model.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;

public class GameServiceTest {

    @Test
    void testCreateValidGame() {
        GameRequest validRequest = new GameRequest("ChessGame");
        Response response = GameService.create(validRequest);

        Assertions.assertTrue(response instanceof GameResponse);
        GameResponse gameResponse = (GameResponse) response;
        Assertions.assertNotNull(gameResponse.gameID());
    }

    @Test
    void testCreateInvalidGame() {
        GameRequest invalidRequest = new GameRequest(null);
        Response response = GameService.create(invalidRequest);

        Assertions.assertTrue(response instanceof FailureResponse);
        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals("Error: bad request", failureResponse.message);
    }


}


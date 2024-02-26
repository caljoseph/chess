package passoffTests.serverTests.service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.*;
import service.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import server.handler.Handler;
import service.GameService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameServiceTest {
    MemoryGameDAO gameDAO = Server.getGameDAO();
    MemoryAuthDAO authDAO = Server.getAuthDAO();
    String auth = "auth";
    @Test
    void testCreateValidGame() {
        GameRequest validRequest = new GameRequest("ChessGame");
        Response response = GameService.create(validRequest);

        assertTrue(response instanceof GameResponse);
        GameResponse gameResponse = (GameResponse) response;
        Assertions.assertNotNull(gameResponse.gameID());
    }

    @Test
    void testCreateInvalidGame() {
        GameRequest invalidRequest = new GameRequest(null);
        Response response = GameService.create(invalidRequest);

        assertTrue(response instanceof FailureResponse);
        FailureResponse failureResponse = (FailureResponse) response;
        assertEquals("Error: bad request", failureResponse.message);
    }

    @BeforeEach
    void setUp() {

    }
    @Test
    void testJoin_invalidRequestFields() {
        authDAO.createAuth(auth, "username");
        JoinGameRequest invalidRequest = new JoinGameRequest(null, null);
        Response response = GameService.join(invalidRequest, auth);

        FailureResponse failureResponse = (FailureResponse) response;
        assertTrue(response instanceof FailureResponse);
        assertEquals("Error: bad request", (failureResponse.message));
    }

    @Test
    public void testJoin_alreadyTakenSpot() {
        authDAO.createAuth(auth, "username");

        GameData gameData = new GameData(1, "Player1", null, "TestGame", null);
        gameDAO.createGame(gameData);

        JoinGameRequest validRequest = new JoinGameRequest("WHITE", "1");
        Response response = GameService.join(validRequest, auth);
        assertTrue(response instanceof FailureResponse);
        assertEquals("Error: already taken", ((FailureResponse) response).getMessage());
    }

    @Test
    public void testJoin_successfulJoin() {
        gameDAO.clear();
        authDAO.createAuth(auth, "username");

        GameData gameData = new GameData(1, null, null, "TestGame", null);
        gameDAO.createGame(gameData);

        JoinGameRequest validRequest = new JoinGameRequest("WHITE", "1");
        Response response = GameService.join(validRequest, auth);
        assertTrue(response instanceof JoinGameResponse);
    }

//    @Test
//    public void testJoin_successfulJoin() {
//        gameDAO.clear();
//        authDAO.createAuth(auth, "username");
//
//        GameData gameData = new GameData(1, null, null, "TestGame", null);
//        gameDAO.createGame(gameData);
//
//        JoinGameRequest validRequest = new JoinGameRequest("WHITE", "1");
//        Response response = GameService.join(validRequest, auth);
//        assertTrue(response instanceof JoinGameResponse);
//    }

}


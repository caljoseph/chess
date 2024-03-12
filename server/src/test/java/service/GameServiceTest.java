package service;

import dataAccess.db.DBAuthDAO;
import dataAccess.db.DBGameDAO;
import dataAccess.db.DBUserDAO;
import model.*;
import model.request.*;
import model.response.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameServiceTest {
    DBGameDAO gameDAO = Server.getGameDAO();
    DBAuthDAO authDAO = Server.getAuthDAO();
    DBUserDAO userDAO = Server.getUserDAO();
    String auth = "auth";
    @BeforeEach
    void setup() {
        gameDAO.clear();
    }
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
        gameDAO = new DBGameDAO();
        gameDAO.clear();
        authDAO = new DBAuthDAO();
        authDAO.clear();
        userDAO = new DBUserDAO();
        userDAO.clear();
    }

    @Test
    void listGamesSuccess() {
        GameData game1 = new GameData(1, "User1", "User2", "ChessGame1", null);
        GameData game2 = new GameData(2, "User3", "User4", "ChessGame2", null);

        gameDAO.createGame(game1);
        gameDAO.createGame(game2);

        var response = GameService.listGames();

        assertTrue(response instanceof ListGamesResponse);
        List<GameData> games = ((ListGamesResponse) response).games();

        assertEquals(2, games.size());
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));
    }

    @Test
    void listGamesEmpty() {
        var response = GameService.listGames();

        assertTrue(response instanceof ListGamesResponse);
        List<GameData> games = ((ListGamesResponse) response).games();

        assertTrue(games.isEmpty());
    }
    @Test
    void testJoin_invalidRequestFields() {
        authDAO.createAuth(auth, "username");
        JoinGameRequest invalidRequest = new JoinGameRequest(null, null);
        Response response = GameService.join(invalidRequest, auth);

        FailureResponse failureResponse = (FailureResponse) response;
        assertTrue(response instanceof FailureResponse);
    }

    @Test
    public void testJoin_alreadyTakenSpot() {
        authDAO.createAuth(auth, "username");

        GameData gameData = new GameData(1, "Player1", null, "TestGame", null);
        gameDAO.createGame(gameData);

        JoinGameRequest validRequest = new JoinGameRequest("WHITE", "1");
        Response response = GameService.join(validRequest, auth);
        assertTrue(response instanceof FailureResponse);
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

    @Test
    public void testJoin_registerFlow(){
        // create user and login
        var registerResult = RegistrationService.register(new RegisterRequest("validUser", "validPass", "validEmail"));
        var registerAuth = ((RegisterResponse) registerResult).authToken;
        var loginResult = UserService.login(new LoginRequest("validUser", "validPass"));
        var loginAuth = ((LoginResponse) loginResult).authToken;
        // create game
        GameRequest validRequest = new GameRequest("ChessGame");
        Response response = GameService.create(validRequest);
        //join game

        var joinResult = GameService.join(new JoinGameRequest("WHITE", "1"), registerAuth);

        var logoutResult = UserService.logout(loginAuth);

    }
    //make some test cases here to see what happens when I log in, logout etc then join a game.
    //it doesn't seem to be recognizing the authtoken I'm passing when I request a new
    //game. So it could be a problem in updating my Auth "database" or else a problem pulling from it
    //or perhaps I'm making duplicate properties
}


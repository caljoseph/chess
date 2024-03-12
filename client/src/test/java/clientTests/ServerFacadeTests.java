package clientTests;

import model.GameData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import ui.exception.ResponseException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static final String SERVER_URL = "http://localhost";

    private static final String GAME_NAME = "TestGame";
    private static final String GAME_ID = "123";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        ServerFacade serverFacade = new ServerFacade(SERVER_URL + String.valueOf(port));
    }
    @BeforeEach
    public void prep() {
        server.getUserDAO().clear();
        server.getGameDAO().clear();
        server.getAuthDAO().clear();
    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void registerSuccess() throws ResponseException {

        assertDoesNotThrow( () -> {
                ServerFacade.register("NewUser", "NewPass", "NewEmail");
        }, "Exception thrown");
    }
    @Test
    public void registerTwoDifferentAccountsSuccess() throws ResponseException {
        ServerFacade.register("OldUser", "OldPass", "OldEmail");
        assertDoesNotThrow( () -> {
            ServerFacade.register("NewUser", "NewPass", "NewEmail");
        }, "Exception thrown");
    }
    @Test
    public void registerFailure() throws ResponseException {
        ServerFacade.register("NewUser", "NewPass", "NewEmail");
        assertThrows(ResponseException.class, () -> {
            // Your function call goes here
            ServerFacade.register("NewUser", "NewPass", "NewEmail");
        }, "Expected ResponseException, but it wasn't thrown");
    }








    @Test
    public void loginSuccess() throws ResponseException {
        ServerFacade.register("validUser", "validPass", "validEmail");
        assertDoesNotThrow(() -> {
            ServerFacade.login("validUser", "validPass");
        }, "Exception thrown");
    }

    @Test
    public void loginFailure() throws ResponseException {
        ServerFacade.register("validUser", "validPass", "validEmail");
        assertThrows(ResponseException.class, () -> {
            ServerFacade.login("invalidUser", "invalidPass");
        }, "Expected ResponseException, but it wasn't thrown");
    }

    @Test
    public void logoutSuccess() throws ResponseException {
        var auth = ServerFacade.register("validUser", "validPass", "validEmail").authToken;

        assertDoesNotThrow(() -> {
            ServerFacade.logout(auth);
        }, "Exception thrown");
    }
    @Test
    public void logoutFailure() throws ResponseException {
        ServerFacade.register("validUser", "validPass", "validEmail");
        assertThrows(ResponseException.class, () -> {
            ServerFacade.logout("invalidAuth");
        }, "Expected ResponseException, but it wasn't thrown");
    }

    @Test
    public void listGamesSuccess() throws ResponseException {
        var auth = ServerFacade.register("validUser", "validPass", "validEmail").authToken;
        List<GameData> games = ServerFacade.listGames(auth).games();
        assertTrue(games instanceof List, "Returned object is not an instance of List");
        assertDoesNotThrow(() -> {
            ServerFacade.listGames(auth);
        }, "Exception thrown");
    }
    @Test
    public void listGamesFailure() throws ResponseException {
        ServerFacade.register("validUser", "validPass", "validEmail");

        assertThrows(ResponseException.class, () -> {
            ServerFacade.listGames("invalidAuth");
        }, "Expected ResponseException, but it wasn't thrown");
    }

    @Test
    public void createGameSuccess() throws ResponseException {
        var auth = ServerFacade.register("validUser", "validPass", "validEmail").authToken;

        assertDoesNotThrow(() -> {
            ServerFacade.createGame(auth, GAME_NAME);
        }, "Exception thrown");
    }
    @Test
    public void createGameFailure() throws ResponseException {
        assertThrows(ResponseException.class, () -> {
            ServerFacade.createGame("invalid Auth", GAME_NAME);
            }, "Expected ResponseException, but it wasn't thrown");
    }

    @Test
    public void joinGameSuccess() throws ResponseException {
        var auth = ServerFacade.register("validUser", "validPass", "validEmail").authToken;
        var gameID = ServerFacade.createGame(auth, GAME_NAME).gameID();
        assertDoesNotThrow(() -> {
            ServerFacade.joinGame(auth, "WHITE", gameID);
        }, "Exception thrown");
    }

    @Test
    public void joinGameFailure() throws ResponseException {
        var auth = ServerFacade.register("validUser", "validPass", "validEmail").authToken;
        var gameID = ServerFacade.createGame(auth, GAME_NAME).gameID();
        assertThrows(ResponseException.class, () -> {
            ServerFacade.joinGame(auth, "blue", GAME_ID + 1);
        }, "Expected ResponseException, but it wasn't thrown");
    }




}

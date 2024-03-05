package dataAccessTests;

import chess.ChessGame;
import dataAccess.db.DBGameDAO;
import dataAccess.db.DBUserDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DBGameDAOTest {
    DBGameDAO gameDAO = new DBGameDAO();
    @BeforeAll
    static void setup() {

    }
    @BeforeEach
    void config() {
        gameDAO.clear();
    }

    @Test
    void successfulAdd() {
        var game = new ChessGame();
        var game_data = new GameData(1,null, null, "gameName", game);
        assert gameDAO.createGame(game_data) instanceof String;
    }
    @Test
    void successfulGetGame() {
        var game = new ChessGame();
        var game_data = new GameData(0,"a white guy", "a black guy", "gameName", game);
        var id = gameDAO.createGame(game_data);
        game_data = new GameData(Integer.parseInt(id), game_data.whiteUsername(),
                        game_data.blackUsername(), game_data.gameName(), game_data.game());

        var result = gameDAO.getGame(id);
        assertTrue(game_data.equals(result));
    }
    @Test
    void unsuccessfulGetGame() {
        var game = new ChessGame();
        var game_data = new GameData(0,"a white guy", "a black guy", "gameName", game);
        var id = gameDAO.createGame(game_data);
        game_data = new GameData(Integer.parseInt(id), game_data.whiteUsername(),
                game_data.blackUsername(), game_data.gameName(), game_data.game());

        var result = gameDAO.getGame(String.valueOf(Integer.parseInt(id) * 2));
        assertFalse(game_data.equals(result));
    }

    @Test
    void successfulListGames() {
        var game = new ChessGame();
        var game_data = new GameData(0,"a white guy", "a black guy", "gameName", game);
        var game_data_expected = new GameData(0,"a white guy", "a black guy", "gameName", game);

        var expectedList = new ArrayList<GameData>();
        for (int i = 0; i < 5; i++) {
            game_data_expected = new GameData(Integer.parseInt(gameDAO.createGame(game_data)), "a white guy", "a black guy", "gameName", game);
            expectedList.add(game_data_expected);
        }

        var result = gameDAO.listGames();
        assertEquals(result, expectedList);
    }
    @Test
    void emptyListGames() {
      assertTrue(gameDAO.listGames().isEmpty());
    }
    @Test
    void successfulUpdate() {
        var game = new ChessGame();
        var game_data = new GameData(1,null, null, "gameName", game);
        var id = gameDAO.createGame(game_data);

        var new_game_data = new GameData(Integer.parseInt(id), "I joined!", null, "gameName", game);
        gameDAO.updateGame(id, new_game_data);

        assertEquals(new_game_data, gameDAO.getGame(id));
    }
    @Test
    void unsuccessfulUpdate() {
        var game = new ChessGame();
        var game_data = new GameData(1,null, null, "gameName", game);
        var id = gameDAO.createGame(game_data);

        var new_game_data = new GameData(Integer.parseInt(id), "I joined!", null, "gameName", game);
        gameDAO.updateGame(String.valueOf(Integer.parseInt(id) * 2), new_game_data);

        assertNotEquals(new_game_data, gameDAO.getGame(id));
    }




}

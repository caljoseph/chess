package dataAccess;

import chess.ChessGame;
import dataAccess.db.DBGameDAO;
import dataAccess.db.DBUserDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        var game_data = new GameData(1,"UN", "UN", "gameName", game);
        assert gameDAO.createGame(game_data) instanceof String;
    }
    @Test
    void unsuccessfulAdd() {

    }


}

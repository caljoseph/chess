package dataAccessTests;

import chess.ChessGame;
import dataAccess.db.DBAuthDAO;
import dataAccess.db.DBGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DBAuthDAOTest {
    DBAuthDAO authDAO = new DBAuthDAO();
    @BeforeAll
    static void setup() {

    }
    @BeforeEach
    void config() {
        authDAO.clear();
    }

    @Test
    void successfulAdd() {
       var auth = authDAO.createAuth("my auth token", "my username");
       assertEquals(auth, authDAO.getAuth(auth).authToken());
    }

    @Test
    void addTwice() {
        var auth = authDAO.createAuth("my auth token", "my username");
        assertNotNull( authDAO.getAuth(auth).authToken());
    }

    @Test
    void getAuthSuccess() {
        var auth = authDAO.createAuth("my auth token", "my username");
        assertEquals(auth, authDAO.getAuth(auth).authToken());
    }
    @Test
    void getAuthFailure() {
        var auth = "not a real auth";
        var result = authDAO.getAuth(auth);
        assertNull(result);
    }
    @Test
    void deleteAuthSuccess() {
        var auth = authDAO.createAuth("my auth token", "my username");
        authDAO.deleteAuth(auth);
        var result = authDAO.getAuth(auth);
        assertNull(result);
    }
    @Test
    void deleteAuthFailure() {
        var auth = authDAO.createAuth("my auth token", "my username");
        authDAO.deleteAuth("different auth");
        var result = authDAO.getAuth(auth);
        assertNotNull(result);
    }

}

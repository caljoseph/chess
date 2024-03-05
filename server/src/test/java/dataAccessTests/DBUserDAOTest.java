package dataAccessTests;
import dataAccess.db.DBUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class DBUserDAOTest {
    DBUserDAO userDAO = new DBUserDAO();
    @BeforeAll
    static void setup() {

    }
    @BeforeEach
    void config() {
        userDAO.clear();
    }

    @Test
    void successfulAddAndGetUser() {
        var newUser = new UserData("newUN", "newPW", "newEM");
        userDAO.createUser(newUser);

        var retrievedUser = userDAO.getUser("newUN");
        assertEquals(newUser, retrievedUser);
    }

    @Test
    void unsuccessfulAddAndGetUser() {
        var newUser = new UserData("newUN", "newPW", "newEM");
        userDAO.createUser(newUser);

        var otherUser = userDAO.getUser("otherUN");
        assertNotEquals(newUser, otherUser);
    }


    @Test
    void successfulVerify() {
        var someUser = new UserData("someUN", "somePW", "someEM");
        userDAO.createUser(someUser);

        var result = userDAO.verifyUser("someUn", "somePW");

        assertTrue(result);
    }
    @Test
    void unsuccessfulVerifyBadUN() {
        var someUser = new UserData("someUN", "somePW", "someEM");
        userDAO.createUser(someUser);

        var result = userDAO.verifyUser("wrongUN", "somePW");

        assertFalse(result);
    }
    @Test
    void unsuccessfulVerifyBadPW() {
        var someUser = new UserData("someUN", "somePW", "someEM");
        userDAO.createUser(someUser);

        var result = userDAO.verifyUser("someUN", "wrongPW");

        assertFalse(result);

    }
}

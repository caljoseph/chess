package service;

import dataAccess.db.DBAuthDAO;
import dataAccess.db.DBGameDAO;
import dataAccess.db.DBUserDAO;
import dataAccess.memory.MemoryAuthDAO;
import dataAccess.memory.MemoryGameDAO;
import dataAccess.memory.MemoryUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilityServiceTest {
    protected static DBAuthDAO authDAO = Server.getAuthDAO();
    protected static DBGameDAO gameDAO = Server.getGameDAO();
    protected static DBUserDAO userDAO = Server.getUserDAO();
    @Test
    public void testClear(){
        authDAO.createAuth("token1", "user1");
        gameDAO.createGame(new GameData(1,null,null,"game",null));
        userDAO.createUser(new UserData("u","p","e"));

        boolean cleared = UtilityService.clear();

        assertTrue(cleared, "UtilityService.clear() should return true");

    }
}

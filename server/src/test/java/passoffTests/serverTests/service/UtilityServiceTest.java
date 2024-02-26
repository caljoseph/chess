package passoffTests.serverTests.service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import server.Server;
import service.UtilityService;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilityServiceTest {
    protected static MemoryAuthDAO authDAO = Server.getAuthDAO();
    protected static MemoryGameDAO gameDAO = Server.getGameDAO();
    protected static MemoryUserDAO userDAO = Server.getUserDAO();
    @Test
    public void testClear(){
        authDAO.createAuth("token1", "user1");
        gameDAO.createGame(new GameData(1,null,null,null,null));
        userDAO.createUser(new UserData("u","p","e"));

        boolean cleared = UtilityService.clear();

        assertTrue(cleared, "UtilityService.clear() should return true");

    }
}

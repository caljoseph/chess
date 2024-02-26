package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import server.Server;

public class UtilityService extends Service{
    public static boolean authenticate(String authToken) {
        if (authDAO.getAuth(authToken) != null) {
            return true;
        } else {
            return false;
        }
    }
    public static boolean clear() {
        var auth = Server.getAuthDAO();
        var game = Server.getGameDAO();
        var user = Server.getUserDAO();
        if (auth.clear() &&
            game.clear() &&
            user.clear()) {
            return true;
        }
        else {
            return false;
        }
    }
}

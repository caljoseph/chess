package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class UtilityService {
    public static boolean clear() {
        var auth = new MemoryAuthDAO();
        var game = new MemoryUserDAO();
        var user = new MemoryUserDAO();
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

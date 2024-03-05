package service;

import dataAccess.db.DBAuthDAO;
import dataAccess.db.DBGameDAO;
import dataAccess.db.DBUserDAO;
import dataAccess.memory.MemoryAuthDAO;
import dataAccess.memory.MemoryGameDAO;
import dataAccess.memory.MemoryUserDAO;
import server.Server;

import java.util.UUID;

public class Service {
    protected static DBAuthDAO authDAO = Server.getAuthDAO();
    protected static DBGameDAO gameDAO = Server.getGameDAO();
    protected static DBUserDAO userDAO = Server.getUserDAO();
    protected static String generateAuth() {
        return UUID.randomUUID().toString();
    }
}

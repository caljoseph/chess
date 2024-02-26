package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import server.Server;

import java.util.UUID;

public class Service {
    protected static MemoryAuthDAO authDAO = Server.getAuthDAO();
    protected static MemoryGameDAO gameDAO = Server.getGameDAO();
    protected static MemoryUserDAO userDAO = Server.getUserDAO();
    protected static String generateAuth() {
        return UUID.randomUUID().toString();
    }
}

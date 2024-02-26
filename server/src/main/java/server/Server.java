package server;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import server.handler.*;
import spark.*;

public class Server {
    static MemoryUserDAO userDAO = new MemoryUserDAO();
    static MemoryAuthDAO authDAO = new MemoryAuthDAO();
    static MemoryGameDAO gameDAO = new MemoryGameDAO();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.init();
        Spark.delete("/db", (req, res) ->
                (ClearHandler.handleRequest(req, res)));
        Spark.post("/user", (req, res) ->
                (RegisterHandler.handleRequest(req, res)));
        Spark.post("/session", (req, res) ->
                (LoginHandler.handleRequest(req, res)));
        Spark.delete("/session", (req, res) ->
                (LogoutHandler.handleRequest(req, res)));
        Spark.get("/game", (req, res) ->
                (ListGamesHandler.handleRequest(req, res)));
        Spark.post("/game", (req, res) ->
                (CreateGameHandler.handleRequest(req, res)));
        Spark.put("/game", (req, res) ->
                (JoinGameHandler.handleRequest(req, res)));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    public static MemoryUserDAO getUserDAO(){
        return userDAO;
    }
    public static MemoryGameDAO getGameDAO(){
        return gameDAO;
    }
    public static MemoryAuthDAO getAuthDAO(){
        return authDAO;
    }
}

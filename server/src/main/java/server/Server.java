package server;

import dataAccess.db.DBAuthDAO;
import dataAccess.db.DBGameDAO;
import dataAccess.db.DBUserDAO;
import dataAccess.memory.MemoryAuthDAO;
import dataAccess.memory.MemoryGameDAO;
import dataAccess.memory.MemoryUserDAO;
import server.handler.*;
import spark.*;

public class Server {
    static DBUserDAO userDAO = new DBUserDAO();
    static DBAuthDAO authDAO = new DBAuthDAO();
    static DBGameDAO gameDAO = new DBGameDAO();

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
    public static DBUserDAO getUserDAO(){
        return userDAO;
    }
    public static DBGameDAO getGameDAO(){
        return gameDAO;
    }
    public static DBAuthDAO getAuthDAO(){
        return authDAO;
    }
}

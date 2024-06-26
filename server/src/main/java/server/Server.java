package server;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.db.DBAuthDAO;
import dataAccess.db.DBGameDAO;
import dataAccess.db.DBUserDAO;
import server.handler.*;
import server.handler.ws.SessionManager;
import server.handler.ws.WebSocketHandler;
import spark.*;

public class Server {
    static DBUserDAO userDAO = new DBUserDAO();
    static DBAuthDAO authDAO = new DBAuthDAO();
    static DBGameDAO gameDAO = new DBGameDAO();
    static SessionManager sessionManager = new SessionManager();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        createDatabase();

        setupWebSocket();

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
    public static void createDatabase(){
        try {
            DatabaseManager.createDatabase();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
    public static SessionManager getSessionManager() { return sessionManager; }
    public static DBUserDAO getUserDAO(){
        return userDAO;
    }
    public static DBGameDAO getGameDAO(){
        return gameDAO;
    }
    public static DBAuthDAO getAuthDAO(){
        return authDAO;
    }

    private void setupWebSocket() {
        Spark.webSocket("/connect", WebSocketHandler.class);
        Spark.init();
    }
}

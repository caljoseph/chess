package server.handler.ws;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.GameDAO;
import dataAccess.db.DBGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import dataAccess.db.DBAuthDAO;
import server.Server;

import java.io.IOException;
import java.util.ArrayList;

@WebSocket
public class WebSocketHandler {
    Gson gson = new Gson();
    SessionManager sessionManager = Server.getSessionManager();
    DBAuthDAO authDAO = Server.getAuthDAO();
    DBGameDAO gameDAO = Server.getGameDAO();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to client");
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Connection closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("Message received: " + message);
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

        String typeStr = jsonObject.get("commandType").getAsString();
        UserGameCommand.CommandType type = UserGameCommand.CommandType.valueOf(typeStr);

        UserGameCommand command;

        switch (type) {
            case JOIN_PLAYER:
                handleJoinPlayer(gson.fromJson(message, JoinPlayer.class), session);
                break;
            case JOIN_OBSERVER:
                handleJoinObserver(gson.fromJson(message, JoinObserver.class));
                break;
            case LEAVE:
                handleLeave(gson.fromJson(message, Leave.class));
                break;
            case MAKE_MOVE:
                handleMakeMove(gson.fromJson(message, MakeMove.class));
                break;
            case RESIGN:
                handleResign(gson.fromJson(message, Resign.class));
                break;
            default:
                throw new IllegalArgumentException("Unhandled server message type: " + type);
        }

    }

    private void handleResign(Resign resign) {
        //Server marks the game as over (no more moves can be made). Game is updated in the database.
        //Server sends a Notification message to all clients in that game informing them that the root client resigned. This applies to both players and observers.
    }

    private void handleMakeMove(MakeMove makeMove) {
        //Server verifies the validity of the move.
        //Game is updated to represent the move. Game is updated in the database.
        //Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
        //Server sends a Notification message to all other clients in that game informing them what move was made.
    }

    private void handleLeave(Leave leave) {
        //If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.
        //Server sends a Notification message to all other clients in that game informing them that the root client left. This applies to both players and observers.
        // In reality i don't think this does anything
    }

    private void handleJoinObserver(JoinObserver joinObserver) {
        //Server sends a LOAD_GAME message back to the root client.
        //Server sends a Notification message to all other clients in that game informing them the root client joined as an observer.
    }

    private void handleJoinPlayer(JoinPlayer joinPlayer, Session session) throws IOException {
        if (authDAO.getAuth(joinPlayer.getAuthString()) == null) {
            sendError(session,"Bad auth bro.");
            return;
        }

        var username = authDAO.getAuth(joinPlayer.getAuthString()).userName();
        var gameId = String.valueOf(joinPlayer.getGameID());

        if (gameDAO.getGame(gameId) == null) {
            sendError(session,"Bad gameID bro.");
            return;
        }


        var gameData = gameDAO.getGame(gameId);
        var loadGame = new LoadGame(gameData.game());

        var joinColor = joinPlayer.getPlayerColor();
        if (gameData.whiteUsername() == null || gameData.blackUsername() == null) {
            sendError(session,"Empty game bro.");
            return;
        }
        if ((joinColor.equals(ChessGame.TeamColor.WHITE) && (!gameData.whiteUsername().equals(username))) ||
                (joinColor.equals(ChessGame.TeamColor.BLACK) && (!gameData.blackUsername().equals(username)))) {
            sendError(session,"Wrong team bro.");
            return;
        }
        session.getRemote().sendString(gson.toJson(loadGame));

        sessionManager.addSession(session, username, gameId);

        for (Session otherSession : sessionManager.getSessionsForGame(gameId)) {
            if (!otherSession.equals(session)) {
                var notification = new Notification(username + " joined the game");
                otherSession.getRemote().sendString(gson.toJson(notification));
            }
        }
        //Server sends a LOAD_GAME message back to the root client.
        //Server sends a Notification message to all other clients in that game informing them what color the root client is joining as.
    }
    void sendError(Session session,String message) throws IOException {
        var error = new Error(message);
        session.getRemote().sendString(gson.toJson(error));
    }
}

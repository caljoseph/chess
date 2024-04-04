package server.handler.ws;

import chess.*;
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
import org.springframework.security.core.parameters.P;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import dataAccess.db.DBAuthDAO;
import server.Server;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

@WebSocket
public class WebSocketHandler {
    Gson gson = new Gson();
    SessionManager sessionManager = Server.getSessionManager();
    DBAuthDAO authDAO = Server.getAuthDAO();
    DBGameDAO gameDAO = Server.getGameDAO();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to client");
        session.setIdleTimeout(1200000);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Connection closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, InvalidMoveException {
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
                handleJoinObserver(gson.fromJson(message, JoinObserver.class), session);
                break;
            case LEAVE:
                handleLeave(gson.fromJson(message, Leave.class), session);
                break;
            case MAKE_MOVE:
                handleMakeMove(gson.fromJson(message, MakeMove.class), session);
                break;
            case RESIGN:
                handleResign(gson.fromJson(message, Resign.class), session);
                break;
            default:
                throw new IllegalArgumentException("Unhandled server message type: " + type);
        }

    }

    private void handleResign(Resign resign, Session session) throws IOException {




        GameData gameData = gameDAO.getGame(String.valueOf(resign.getGameID()));
        ChessGame game = gameData.game();
        var username = authDAO.getAuth(resign.getAuthString()).userName();

        var observerError = new Error("You don't have permission to resign");
        if (!username.equals(gameData.blackUsername()) && !username.equals(gameData.whiteUsername())) {
            session.getRemote().sendString(gson.toJson(observerError));
            return;
        }
        var alreadyOverError = new Error("The game is already over");
        if (game.isOver()) {
            session.getRemote().sendString(gson.toJson(alreadyOverError));
            return;
        }

        game.setOver(true);
        gameDAO.updateGame(String.valueOf(resign.getGameID()), new GameData(gameData, game));

        for (Session s : sessionManager.getSessionsForGame(String.valueOf(resign.getGameID()))) {
            if (s.isOpen()) {
                var notification = new Notification("\n" + username + " left the game.");
                s.getRemote().sendString(gson.toJson(notification));
            }
        }
        if (session.isOpen()) {
            session.close();
        }

        sessionManager.removeSession(session);
    }

    private void handleMakeMove(MakeMove makeMove, Session session) throws IOException, InvalidMoveException {


        ChessMove move = makeMove.getMove();
        String gameID = String.valueOf(makeMove.getGameID());
        GameData gameData = gameDAO.getGame(gameID);
        ChessGame game = gameData.game();
        ChessBoard board = game.getBoard();
        ChessPosition position = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        if (game.isOver()) {
            sendError(session,"The game is over");
            return;
        }


        if (gameData.blackUsername() == null || gameData.whiteUsername() == null) {
            sendError(session,"Please wait for an opponent to join");
            return;
        }


        // Check if empty square
        if (board.getPiece(position) == null) {
            sendError(session,"You cannot move an empty square");
            return;
        }

        ChessGame.TeamColor selectedPieceColor = board.getPiece(position).getTeamColor();
        ChessPiece.PieceType selectedPieceType = board.getPiece(position).getPieceType();
        String username = authDAO.getAuth(makeMove.getAuthString()).userName();
        String otherPlayerUsername = (selectedPieceColor == ChessGame.TeamColor.WHITE) ? gameData.blackUsername() : gameData.whiteUsername();

        //Check if I'm trying to move my opponent
        if ((selectedPieceColor.equals(ChessGame.TeamColor.WHITE) && !gameData.whiteUsername().equals(username))
                || (selectedPieceColor.equals(ChessGame.TeamColor.BLACK) && !gameData.blackUsername().equals(username))) {
            sendError(session,"You cannot move your opponents pieces");
            return;
        }

        Collection<ChessMove> validMoves = game.validMoves(position);
        try {
            game.makeMove(move);
            gameDAO.updateGame(gameID, new GameData(gameData, game));
            var loadGame = new LoadGame(gameData.game());
            var notification = new Notification(username + " [\u001B[0m" + selectedPieceColor + "]\u001B[0m moved their \u001B[0m" + selectedPieceType + "\u001B[0m from " + position.prettyString() + " to " + end.prettyString());
            var checkNotification = new Notification(otherPlayerUsername + " [\u001B[0m" + selectedPieceColor + "]\u001B[0 is in check");
            var checkMateNotification = new Notification(otherPlayerUsername + " [\u001B[0m" + selectedPieceColor + "]\u001B[0m moved their \u001B[0m" + selectedPieceType + "\u001B[0m from " + position.prettyString() + " to " + end.prettyString());

            for (Session s : sessionManager.getSessionsForGame(gameID)) {
                if (s.isOpen()) {
                    s.getRemote().sendString(gson.toJson(loadGame));
                }
                if (!s.equals(session) && s.isOpen()) {
                    s.getRemote().sendString(gson.toJson(notification));
                }
                if (game.isInCheck(game.otherTeam(selectedPieceColor))){
                    s.getRemote().sendString(gson.toJson(checkNotification));
                }
                if (game.isInCheckmate(game.otherTeam(selectedPieceColor))){
                    s.getRemote().sendString(gson.toJson(checkMateNotification));
                }
            }
        } catch (InvalidMoveException e) {
            sendError(session,e.getMessage());
        }



        //Server verifies the validity of the move.
        //Game is updated to represent the move. Game is updated in the database.
        //Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
        //Server sends a Notification message to all other clients in that game informing them what move was made.
    }

    private void handleLeave(Leave leave, Session session) throws IOException {
        String gameId = String.valueOf(leave.getGameID());
        var username = authDAO.getAuth(leave.getAuthString()).userName();
        var game = gameDAO.getGame(gameId);
        String white = (leave.getPlayerColor() == ChessGame.TeamColor.WHITE) ? null : game.whiteUsername();
        String black = (leave.getPlayerColor() == ChessGame.TeamColor.BLACK) ? null : game.blackUsername();

        gameDAO.updateGame(gameId, new GameData(game, white, black));

        if (session.isOpen()) {
            session.close();
        }

        sessionManager.removeSession(session);

        for (Session otherSession : sessionManager.getSessionsForGame(gameId)) {
            if (!otherSession.equals(session) && otherSession.isOpen()) { // This check might be redundant now since the session is closed
                var notification = new Notification("\n" + username + " left the game.");
                otherSession.getRemote().sendString(gson.toJson(notification));
            }
        }
        //If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.
        //Server sends a Notification message to all other clients in that game informing them that the root client left. This applies to both players and observers.

    }

    private void handleJoinObserver(JoinObserver joinObserver, Session session) throws IOException {
        if (authDAO.getAuth(joinObserver.getAuthString()) == null) {
            sendError(session,"Bad auth bro.");
            return;
        }

        var username = authDAO.getAuth(joinObserver.getAuthString()).userName();
        var gameId = String.valueOf(joinObserver.getGameID());

        if (gameDAO.getGame(gameId) == null) {
            sendError(session,"Bad gameID bro.");
            return;
        }

        var gameData = gameDAO.getGame(gameId);
        var loadGame = new LoadGame(gameData.game());

        session.getRemote().sendString(gson.toJson(loadGame));

        sessionManager.addSession(session, username, gameId);

        for (Session otherSession : sessionManager.getSessionsForGame(gameId)) {
            if (!otherSession.equals(session) && otherSession.isOpen()) {
                var notification = new Notification("\n" + username + " joined the game as an observer");
                otherSession.getRemote().sendString(gson.toJson(notification));
            }
        }
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
        if (gameData.whiteUsername() == null && gameData.blackUsername() == null) {
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
                if (otherSession.isOpen()) {
                    var notification = new Notification("\n" + username + " joined the game");
                    otherSession.getRemote().sendString(gson.toJson(notification));
                }
            }
        }
    }
    void sendError(Session session,String message) throws IOException {
        var error = new Error(message);
        session.getRemote().sendString(gson.toJson(error));
    }
}

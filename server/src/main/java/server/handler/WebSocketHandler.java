package server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@WebSocket
public class WebSocketHandler {
    Gson gson = new Gson();


    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to client");
        try {

            var message = gson.toJson(new Notification("This notification says hi!!"));
            session.getRemote().sendString(message);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Connection closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message received: " + message);
        JsonObject jsonObject = gson.fromJson(message, JsonObject.class);

        String typeStr = jsonObject.get("commandType").getAsString();
        UserGameCommand.CommandType type = UserGameCommand.CommandType.valueOf(typeStr);

        UserGameCommand command;

        switch (type) {
            case JOIN_PLAYER:
                command = gson.fromJson(message, JoinPlayer.class);
                break;
            case JOIN_OBSERVER:
                command = gson.fromJson(message, JoinObserver.class);
                break;
            case LEAVE:
                command = gson.fromJson(message, Leave.class);
                break;
            case MAKE_MOVE:
                command = gson.fromJson(message, MakeMove.class);
                break;
            case RESIGN:
                command = gson.fromJson(message, Resign.class);
                break;
            default:
                throw new IllegalArgumentException("Unhandled server message type: " + type);
        }

    }
}

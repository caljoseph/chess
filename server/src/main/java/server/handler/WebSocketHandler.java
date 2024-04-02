package server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;

@WebSocket
public class WebSocketHandler {
    Gson gson = new Gson();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to client");
        // Attempt to send a message back to the client after the connection is established
        try {
            var message = gson.toJson(new Notification("This notification says hi!!"));
            session.getRemote().sendString(message);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error - possibly logging or cleaning up if needed
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Connection closed");
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message received: " + message);
        // Here you can also send a message back to the client
    }
}

package server.handler;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;

@WebSocket
public class WebSocketHandler {

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("Connected to client");
        // Attempt to send a message back to the client after the connection is established
        try {
            session.getRemote().sendString("Hello from the server!");
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

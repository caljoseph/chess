package server.handler.ws;
import org.eclipse.jetty.websocket.api.Session;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SessionManager {
    private final Map<Session, String> sessionToUser = new HashMap<>();
    private final Map<String, Set<Session>> gameToSessions = new HashMap<>();

    public synchronized void addSession(Session session, String username, String gameId) {
        sessionToUser.put(session, username);
        gameToSessions.computeIfAbsent(gameId, k -> new HashSet<>()).add(session);
    }

    public synchronized void removeSession(Session session) {
        String username = sessionToUser.remove(session);
        gameToSessions.values().forEach(sessions -> sessions.remove(session));
    }

    public synchronized Set<Session> getSessionsForGame(String gameId) {
        return gameToSessions.getOrDefault(gameId, new HashSet<>());
    }
}


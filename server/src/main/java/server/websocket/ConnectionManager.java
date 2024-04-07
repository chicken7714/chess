package server.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, HashMap<String, Connection>> connections = new ConcurrentHashMap<>();

    public void addGameToMap(int gameID) {
        connections.put(gameID, new HashMap<>());
    }
    public void addSessionToGame(int gameID, String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.get(gameID).put(authToken, connection);
    }

    public void removeSessionFromGame(int gameID, String authToken) {
        connections.get(gameID).remove(authToken);
    }

    public void broadcast(int gameID, String excludeAuth, ServerMessage notification) throws IOException {
        var authRemoveList = new ArrayList<String>();
        for (var entry : connections.get(gameID).entrySet()) {
            var c = entry.getValue();
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuth)) {
                    c.send(notification.toString());
                }
            } else {
                authRemoveList.add(entry.getKey());
            }
        }

        for (var c : authRemoveList) {
            connections.get(gameID).remove(c);
        }
    }
}

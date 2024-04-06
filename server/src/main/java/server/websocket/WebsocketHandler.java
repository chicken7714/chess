package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebsocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(userGameCommand);
            case JOIN_OBSERVER -> joinObserver(userGameCommand);
            case MAKE_MOVE -> makeMove(userGameCommand);
            case LEAVE -> leave(userGameCommand);
            case RESIGN -> resign(userGameCommand);
        }
    }

    private void joinPlayer(UserGameCommand joinPlayerCommand) {
        // Server sends a LOAD_GAME message back to the root client.
        // Server sends a Notification message to all other clients in
        // that game informing them what color the root client is joining as.
    }

    private void joinObserver(UserGameCommand joinObserverCommand) {
        //Server sends a LOAD_GAME message back to the root client.
        //Server sends a Notification message to all other clients
        // in that game informing them the root client joined as an observer.
    }

    private void makeMove(UserGameCommand makeMoveCommand) {
        // Server verifies the validity of the move.

        // Game is updated to represent the move. Game is updated in the database.

        // Server sends a LOAD_GAME message to all clients in the game (including the root client)
        // with an updated game.

        // Server sends a Notification message to all other clients in that game informing
        // them what move was made.
    }

    private void leave(UserGameCommand leaveCommand) {
        // If a player is leaving, then the game is updated to remove the root client.
        // Game is updated in the database.

        // Server sends a Notification message to all other clients in that game
        // informing them that the root client left. This applies to both players and observers.
    }

    private void resign(UserGameCommand resignCommand) {
        // Server marks the game as over (no more moves can be made).
        // Game is updated in the database.

        //Server sends a Notification message to all clients in
        // that game informing them that the root client resigned.
        // This applies to both players and observers.
    }
}

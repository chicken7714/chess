package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.GameModel;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.InvalidRequestException;
import service.WebSocketServices;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebsocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer((JoinPlayerCommand) userGameCommand);
            case JOIN_OBSERVER -> joinObserver((JoinObserverCommand) userGameCommand);
            case MAKE_MOVE -> makeMove(userGameCommand);
            case LEAVE -> leave(userGameCommand);
            case RESIGN -> resign(userGameCommand);
        }
    }

    private void joinPlayer(JoinPlayerCommand joinPlayerCommand) throws IOException {
        String auth = joinPlayerCommand.getAuthString();
        int gameID = joinPlayerCommand.getGameID();
        ChessGame.TeamColor teamColor = joinPlayerCommand.getTeamColor();

        // Server sends a LOAD_GAME message back to the root client.
        try {
            GameService service = new GameService();
            String gameJson = service.getGame(gameID);
            GameModel gameModel = new Gson().fromJson(gameJson, GameModel.class);

            String username = null;
            String teamColorString = null;
            if (teamColor == ChessGame.TeamColor.WHITE) {
                username = gameModel.whiteUsername();
                teamColorString = "WHITE";
            } else if (teamColor == ChessGame.TeamColor.BLACK) {
                username = gameModel.blackUsername();
                teamColorString = "BLACK";
            }

            ServerMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(loadGameMessage));
            // Server sends a Notification message to all other clients in
            // that game informing them what color the root client is joining as.
            connectionManager.broadcast(gameID, auth, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s had joined as %s", username, teamColorString)));
        } catch (InvalidRequestException e) {
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid Game ID");
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(errorMessage));
        }
    }

    private void joinObserver(JoinObserverCommand joinObserverCommand) throws IOException {
        String auth = joinObserverCommand.getAuthString();
        int gameID = joinObserverCommand.getGameID();
        WebSocketServices service = new WebSocketServices();

        try {
            String username = service.getUsername(auth);

            GameService gameService = new GameService();
            String gameJson = gameService.getGame(gameID);

            //Server sends a LOAD_GAME message back to the root client.
            ServerMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson())
            //Server sends a Notification message to all other clients
            // in that game informing them the root client joined as an observer.
            ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has joined as an observer", username));
            connectionManager.broadcast(gameID, auth, notification);
        } catch (InvalidRequestException | DataAccessException e) {
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid request");
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(errorMessage));
        }
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

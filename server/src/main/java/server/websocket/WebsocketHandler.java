package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@WebSocket
public class WebsocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, message);
            case JOIN_OBSERVER -> joinObserver(session, message);
            case MAKE_MOVE -> makeMove(session, message);
            case LEAVE -> leave(session, message);
            case RESIGN -> resign(session, message);
        }
    }

    private void joinPlayer(Session session, String message) throws IOException {
        JoinPlayerCommand joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);
        String auth = joinPlayerCommand.getAuthString();
        int gameID = joinPlayerCommand.getGameID();
        ChessGame.TeamColor teamColor = joinPlayerCommand.getTeamColor();
        connectionManager.connections.putIfAbsent(gameID, new HashMap<>());
        connectionManager.addSessionToGame(gameID, auth, session);

        // Server sends a LOAD_GAME message back to the root client.
        try {
            WebSocketServices service = new WebSocketServices();
            String gameJson = service.getGame(gameID);
            GameModel gameModel = new Gson().fromJson(gameJson, GameModel.class);

            String username = service.getUsername(auth);
            if (username == null) {
                throw new InvalidRequestException("Invalid auth token");
            }

            String teamColorString = null;
            if (teamColor == ChessGame.TeamColor.WHITE && !username.equals(gameModel.whiteUsername())) {
                throw new InvalidRequestException("White username already taken");
            } else if (teamColor == ChessGame.TeamColor.BLACK && !username.equals(gameModel.blackUsername())) {
                throw new InvalidRequestException("Black username already taken");
            } else if (teamColor == ChessGame.TeamColor.WHITE) {
                teamColorString = "WHITE";
            } else if (teamColor == ChessGame.TeamColor.BLACK) {
                teamColorString = "BLACK";
            }

            ServerMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(loadGameMessage));
            // Server sends a Notification message to all other clients in
            // that game informing them what color the root client is joining as.

            connectionManager.broadcast(gameID, auth, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s had joined as %s", username, teamColorString)));
        } catch (DataAccessException | InvalidRequestException e) {
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid Game ID");
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(errorMessage));
        }
    }

    private void joinObserver(Session session, String message) throws IOException {
        JoinObserverCommand joinObserverCommand = new Gson().fromJson(message, JoinObserverCommand.class);
        String auth = joinObserverCommand.getAuthString();
        int gameID = joinObserverCommand.getGameID();
        WebSocketServices service = new WebSocketServices();
        connectionManager.connections.putIfAbsent(gameID, new HashMap<>());
        connectionManager.addSessionToGame(gameID, auth, session);

        try {
            String username = service.getUsername(auth);
            if (username == null) {
                throw new InvalidRequestException("Invalid auth token");
            }

            String gameJson = service.getGame(gameID);

            //Server sends a LOAD_GAME message back to the root client.
            ServerMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameJson);
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(loadGameMessage));
            //Server sends a Notification message to all other clients
            // in that game informing them the root client joined as an observer.
            ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, String.format("%s has joined as an observer", username));
            connectionManager.broadcast(gameID, auth, notification);
        } catch (InvalidRequestException | DataAccessException e) {
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid request");
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(errorMessage));
        }
    }

    private void makeMove(Session session, String message) throws IOException {
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        ChessMove move = makeMoveCommand.getMove();
        int gameID = makeMoveCommand.getGameID();
        String auth = makeMoveCommand.getAuthString();

        WebSocketServices service = new WebSocketServices();
        Gson gson = new GsonBuilder().serializeNulls().create();

        // Server verifies the validity of the move.
        try {
            String oldGameJson = service.getGame(gameID);
            GameModel gameModel = gson.fromJson(oldGameJson, GameModel.class);
            ChessGame game = gameModel.game();
            String username = service.getUsername(auth);

            ChessGame.TeamColor turn = game.getTeamTurn();

            if (!username.equals(gameModel.whiteUsername()) && turn.equals(ChessGame.TeamColor.WHITE)) {
                throw new InvalidRequestException("Not your place bro");
            } else if (!username.equals(gameModel.blackUsername()) && turn.equals(ChessGame.TeamColor.BLACK)) {
                throw new InvalidRequestException("Not your place bro");
            }

            game.makeMove(move);

            // Game is updated to represent the move. Game is updated in the database.
            GameModel newGameModel = new GameModel(gameModel.gameID(), gameModel.whiteUsername(), gameModel.blackUsername(), gameModel.gameName(), game);
            String newGameJson = gson.toJson(newGameModel);
            service.updateGame(gameID, newGameJson);

            // Server sends a LOAD_GAME message to all clients in the game (including the root client)
            // with an updated game.
            ServerMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, newGameJson);
            var users = connectionManager.connections.get(gameID);
            for (var user : users.entrySet()) {
                user.getValue().send(new Gson().toJson(loadGameMessage));
            }

            // Server sends a Notification message to all other clients in that game informing
            // them what move was made.
            ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, move + " was made");
            connectionManager.broadcast(gameID, auth, notification);
        } catch (InvalidRequestException | DataAccessException | InvalidMoveException e) {
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid request");
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(errorMessage));
        }
    }

    private void leave(Session session, String message) throws IOException {
        LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
        int gameID = leaveCommand.getGameID();
        String auth = leaveCommand.getAuthString();
        WebSocketServices service = new WebSocketServices();
        Gson gson = new GsonBuilder().serializeNulls().create();

        // If a player is leaving, then the game is updated to remove the root client.
        // Game is updated in the database.
        try {
            String username = service.getUsername(auth);
            String oldGameJson = service.getGame(gameID);
            GameModel gameModel = gson.fromJson(oldGameJson, GameModel.class);

            if (gameModel.whiteUsername().equals(username)) {
                GameModel newGameModel = new GameModel(gameID, null, gameModel.blackUsername(), gameModel.gameName(), gameModel.game());
                service.updateGame(gameID, gson.toJson(newGameModel));
            } else if (gameModel.blackUsername().equals(username)) {
                GameModel newGameModel = new GameModel(gameID, gameModel.whiteUsername(), null, gameModel.gameName(), gameModel.game());
                service.updateGame(gameID, gson.toJson(newGameModel));
            } else {
                ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid Username leaving");
                connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(errorMessage));
            }

            // Server sends a Notification message to all other clients in that game
            // informing them that the root client left. This applies to both players and observers.
            connectionManager.removeSessionFromGame(gameID, auth);
            ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left the game.");
            connectionManager.broadcast(gameID, auth, notification);
        } catch (DataAccessException | InvalidRequestException e) {
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid username leaving game");
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(errorMessage));
        }
    }

    private void resign(Session session, String message) throws IOException {
        ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
        int gameID = resignCommand.getGameID();
        String auth = resignCommand.getAuthString();
        WebSocketServices service = new WebSocketServices();
        Gson gson = new GsonBuilder().serializeNulls().create();

        try {
            // Server marks the game as over (no more moves can be made).
            // Game is updated in the database.
            String gameJson = service.getGame(gameID);
            GameModel gameModel = gson.fromJson(gameJson, GameModel.class);
            ChessGame oldGame = gameModel.game();
            String username = service.getUsername(auth);
            if (!(username.equals(gameModel.blackUsername()) || username.equals(gameModel.whiteUsername()))) {
                throw new InvalidRequestException("Not your place bro");
            } else if (oldGame.getTeamTurn() == null) {
                throw new InvalidRequestException("Other player has already lost/resigned");
            }

            oldGame.setTeamTurn(null);
            GameModel newGameModel = new GameModel(gameID, gameModel.whiteUsername(), gameModel.whiteUsername(), gameModel.gameName(), oldGame);
            service.updateGame(gameID, gson.toJson(newGameModel));

            //Server sends a Notification message to all clients in
            // that game informing them that the root client resigned.
            // This applies to both players and observers.
            ServerMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " has resigned from the game.");
            var users = connectionManager.connections.get(gameID); //gives HashMap with keys = authTokens, values = Connections
            for (var user : users.entrySet()) {
                user.getValue().send(new Gson().toJson(notification));
            }
        } catch (DataAccessException | InvalidRequestException e) {
            ServerMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid resignation");
            connectionManager.connections.get(gameID).get(auth).send(new Gson().toJson(errorMessage));
        }
    }
}

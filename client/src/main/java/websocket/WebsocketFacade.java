package websocket;

import ServerFacade.ResponseException;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketFacade extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;

    public WebsocketFacade(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    serverMessageHandler.notify(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor teamColor) throws ResponseException {
        try {
            UserGameCommand joinPlayerCommand = new JoinPlayerCommand(authToken, gameID, teamColor);
            Gson gson = new GsonBuilder().serializeNulls().create();
            this.session.getBasicRemote().sendText(gson.toJson(joinPlayerCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID) throws ResponseException {
        try {
            UserGameCommand joinObserverCommand = new JoinObserverCommand(authToken, gameID);
            Gson gson = new GsonBuilder().serializeNulls().create();
            this.session.getBasicRemote().sendText(gson.toJson(joinObserverCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            UserGameCommand makeMoveCommand = new MakeMoveCommand(authToken, gameID, move);
            Gson gson = new GsonBuilder().serializeNulls().create();
            this.session.getBasicRemote().sendText(gson.toJson(makeMoveCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            UserGameCommand leaveCommand = new LeaveCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(gson.toJson(leaveCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void resignGame(String authToken, int gameID) throws ResponseException {
        try {
            Gson gson = new GsonBuilder().serializeNulls().create();
            UserGameCommand resignCommand = new ResignCommand(authToken, gameID);

            this.session.getBasicRemote().sendText(gson.toJson(resignCommand));
        } catch (IOException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}

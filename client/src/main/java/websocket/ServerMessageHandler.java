package websocket;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageHandler {
    void notify(ServerMessage serverMessage);

    void updateGame(ChessGame game);
}

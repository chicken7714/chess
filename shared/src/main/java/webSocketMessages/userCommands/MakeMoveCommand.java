package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private int gameID;
    private ChessMove move;

    public MakeMoveCommand(String authToken, int gameID, ChessMove move) {
        super(authToken);

        this.gameID = gameID;
        this.move = move;
        this.commandType = CommandType.MAKE_MOVE;
    }

    public ChessMove getMove() {
        return this.move;
    }

    public int getGameID() {
        return this.gameID;
    }
}

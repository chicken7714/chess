package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private int gameID;

    public JoinObserverCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;

        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public int getGameID() {
        return this.gameID;
    }
}

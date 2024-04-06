package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private int gameID;

    public LeaveCommand(String authToken, int gameID) {
        super(authToken);

        this.gameID = gameID;
        this.commandType = CommandType.LEAVE;
    }

    public int getGameID() {
        return this.gameID;
    }
}

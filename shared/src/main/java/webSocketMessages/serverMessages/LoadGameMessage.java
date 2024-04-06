package webSocketMessages.serverMessages;

public class LoadGameMessage extends ServerMessage {
    private Object game;

    public LoadGameMessage(ServerMessageType type, Object game) {
        super(type);

        this.game = game;
    }

    public Object getGame() {
        return this.game;
    }
}

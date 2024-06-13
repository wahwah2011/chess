package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    private final String game;

    public LoadGameMessage(ServerMessageType type, String game) {
        super(type);
        this.game = game;
    }

    public String getGame() {
        return game;
    }
}

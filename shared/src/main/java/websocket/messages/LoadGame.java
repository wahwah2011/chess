package websocket.messages;

public class LoadGame extends ServerMessage {
    public String game;

    public LoadGame(ServerMessageType type, String game) {
        super(type);
        this.game = game;
    }
}

package websocket.messages;

public class Error extends ServerMessage {
    private String errorMessage;
    private static final String ERROR_HEADER = "Error: ";

    public Error(ServerMessageType type) {
        super(type);
    }
}

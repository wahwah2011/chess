package websocket.messages;

public class ErrorMessage extends ServerMessage {
    private String errorMessage;
    private static final String ERROR_HEADER = "Error: ";

    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}

package websocket.commands;

public class ConnectCommand extends UserGameCommand{
    public ConnectCommand(String authToken, Integer gameID) {
        super(authToken, gameID);
    }
}

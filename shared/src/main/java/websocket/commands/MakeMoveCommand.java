package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    private ChessMove move;

    public MakeMoveCommand(String authToken, Integer gameID) {
        super(authToken, gameID);
    }
}

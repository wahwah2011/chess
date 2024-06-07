import chess.*;
import ui.ChessClient;

import java.io.IOException;

public class Main {
    private String authToken;

    public static void main(String[] args) throws IOException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        //displays menu object
        ChessClient client = new ChessClient();
        client.run();
    }
}
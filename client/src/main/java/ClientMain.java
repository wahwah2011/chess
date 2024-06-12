import chess.*;
import client.ChessClient;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        //displays menu object
        ChessClient client = null;
        try {
            client = new ChessClient(8080);
            client.run();
        } catch (Exception e) {
            System.out.println("Unable to connect to client");
        }
    }
}
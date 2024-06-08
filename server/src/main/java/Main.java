import chess.*;
import dataaccess.DatabaseManager;
import server.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        //call createDatabase from database manager in here?
        DatabaseManager db = new DatabaseManager();

        Server myServer = new Server();
        myServer.run(0);

    }
}
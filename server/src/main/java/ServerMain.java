import chess.*;
import server.*;

public class ServerMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        //call createDatabase from database manager in here?
        Server myServer = new Server();
        myServer.run(8080);
    }
}
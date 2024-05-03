package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {
    private ChessPiece pieceInQuestion;

    public PieceMovesCalculator(ChessPiece piece) {
        pieceInQuestion = piece;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.KING) {
            System.out.println("KING");
        }
        else if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.QUEEN) {
            System.out.println("QUEEN");
        }
        else if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.BISHOP) {
            System.out.println("BISHOP");
        }
        return new ArrayList<ChessMove>();
    }

}

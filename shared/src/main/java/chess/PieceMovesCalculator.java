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
            KingMovesCalculator kMoves = new KingMovesCalculator(board, position);
            return kMoves.pieceMoves();
        } else if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.QUEEN) {
            QueenMovesCalculator qMoves = new QueenMovesCalculator(board, position);
            return qMoves.pieceMoves();
        } else if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.BISHOP) {
            BishopMovesCalculator bMoves = new BishopMovesCalculator(board, position);
            return bMoves.pieceMoves();
        } else if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.KNIGHT) {
            KnightMovesCalculator nMoves = new KnightMovesCalculator(board, position);
            return nMoves.pieceMoves();
        } else if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.ROOK) {
            RookMovesCalculator rMoves = new RookMovesCalculator(board, position);
            return rMoves.pieceMoves();
        } else if (pieceInQuestion.getPieceType() == ChessPiece.PieceType.PAWN) {
            PawnMovesCalculator pMoves = new PawnMovesCalculator(board, position);
            return pMoves.pieceMoves();
        }
        return new ArrayList<>();
    }
}
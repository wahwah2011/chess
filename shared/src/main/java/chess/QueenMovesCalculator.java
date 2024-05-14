package chess;

import java.util.ArrayList;

public class QueenMovesCalculator extends PieceMovesCalculator {
    public QueenMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public ArrayList<ChessMove> queenMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(diagonal());
        moves.addAll(horizontal());
        return moves;
    }
}

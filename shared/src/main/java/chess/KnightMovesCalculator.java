package chess;

import java.util.ArrayList;

public class KnightMovesCalculator extends PieceMovesCalculator {
    public KnightMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public ArrayList<ChessMove> knightMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>(nMoves());
        return moves;
    }
}

package chess;

import java.util.ArrayList;

public class RookMovesCalculator extends PieceMovesCalculator {
    public RookMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public ArrayList<ChessMove> rookMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(horizontal());
        return moves;
    }
}

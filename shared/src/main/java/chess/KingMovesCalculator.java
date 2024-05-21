package chess;

import java.util.ArrayList;

public class KingMovesCalculator extends PieceMovesCalculator {
    public KingMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public ArrayList<ChessMove> kingMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>(kingMove());
        return moves;
    }
}

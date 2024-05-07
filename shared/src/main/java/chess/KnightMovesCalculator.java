package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {

    public KnightMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public Collection<ChessMove> knightMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(nMoves());
        return moves;
    }
}
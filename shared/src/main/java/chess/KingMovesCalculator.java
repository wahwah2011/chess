package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public KingMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }


    public Collection<ChessMove> kingMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(kMoves());
        return moves;
    }
}

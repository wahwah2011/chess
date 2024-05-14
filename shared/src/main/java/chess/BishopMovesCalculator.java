package chess;

import java.util.ArrayList;

public class BishopMovesCalculator extends PieceMovesCalculator {

    public BishopMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public ArrayList<ChessMove> bishopMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        moves.addAll(diagonal());
        return moves;
    }
}

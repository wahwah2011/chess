package chess;

import java.util.ArrayList;

public class PawnMovesCalculator extends PieceMovesCalculator {

    public PawnMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
    }

    public ArrayList<ChessMove> pawnMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>(pawnMoves(this.getColor()));
        return moves;
    }
}

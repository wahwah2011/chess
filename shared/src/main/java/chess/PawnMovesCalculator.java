package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {

    private final ChessGame.TeamColor color;

    public PawnMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        super(board, position, color);
        this.color = color;
    }

    public Collection<ChessMove> pawnMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        if (color == ChessGame.TeamColor.WHITE) {
            moves.addAll(whitePMoves());
        }
        else if (color == ChessGame.TeamColor.BLACK) {
            moves.addAll(blackPMoves());
        }

        return moves;
    }
}
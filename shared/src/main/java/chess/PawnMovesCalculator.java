package chess;

import java.util.Collection;

public class PawnMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;

    public PawnMovesCalculator(ChessBoard board,ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> pieceMoves() {
        //implement using private class variables
        throw new RuntimeException("Not implemented");
    }
}

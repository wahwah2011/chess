package chess;

import java.util.Collection;

public class KnightMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;

    public KnightMovesCalculator(ChessBoard board,ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> pieceMoves() {
        //implement using private class variables
        throw new RuntimeException("Not implemented");
    }
}

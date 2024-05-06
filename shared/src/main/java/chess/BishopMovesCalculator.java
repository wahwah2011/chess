package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;

    public BishopMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> pieceMoves() {
        List<ChessMove> moves = new ArrayList<>();

        // Check diagonally in four directions
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];
            int x = position.getRow() + dx;
            int y = position.getColumn() + dy;
            while (ChessPosition.isValidPosition(x, y) && !board.hasPiece(new ChessPosition(x, y))) {
                moves.add(new ChessMove(position, new ChessPosition(x, y), null));
                x += dx;
                y += dy;
            }
            // Check if the diagonal movement ends with a capture
            if (ChessPosition.isValidPosition(x, y) && board.getPiece(new ChessPosition(x, y)).getTeamColor() != board.getPiece(position).getTeamColor()) {
                moves.add(new ChessMove(position, new ChessPosition(x, y), null));
            }
        }

        return moves;
    }
}
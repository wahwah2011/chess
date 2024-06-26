package chess;

import java.util.ArrayList;

public class PieceMovesCalculator {

    private final ChessBoard board;
    private final ChessPosition position;
    private final ChessGame.TeamColor color;

    public PieceMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        this.board = board;
        this.position = position;
        this.color = color;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    public ArrayList<ChessMove> horizontal() {
        int[][] directions = {
                {1, 0},  // up
                {-1, 0}, // down
                {0, 1},  // right
                {0, -1}  // left
        };
        return generateMoves(directions);
    }

    public ArrayList<ChessMove> diagonal() {
        int[][] directions = {
                {1, 1},  // up-right
                {1, -1}, // up-left
                {-1, 1}, // down-right
                {-1, -1} // down-left
        };
        return generateMoves(directions);
    }

    private ArrayList<ChessMove> generateMoves(int[][] directions) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        for (int[] direction : directions) {
            int newRow = row;
            int newCol = col;
            while (true) {
                newRow += direction[0];
                newCol += direction[1];
                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                    break; // Out of board bounds
                }
                ChessPosition end = new ChessPosition(newRow, newCol);
                if (board.hasPiece(end)) {
                    if (board.getPiece(end).getTeamColor() != color) {
                        moves.add(new ChessMove(start, end));
                    }
                    break; // Stop if there's a piece in the way
                } else {
                    moves.add(new ChessMove(start, end));
                }
            }
        }

        return moves;
    }

    public ArrayList<ChessMove> kingMove() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        // All possible moves a king can make
        int[][] moveOffsets = {
                {1, 1}, {1, -1}, {-1, -1}, {-1, 1},
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        addMovesFromOffsets(moves, moveOffsets, start);

        return moves;
    }

    public ArrayList<ChessMove> knightMove() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        // All possible moves a knight can make
        int[][] moveOffsets = {
                {2, 1}, {2, -1}, {1, 2}, {1, -2},
                {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}
        };

        addMovesFromOffsets(moves, moveOffsets, start);

        return moves;
    }

    private void addMovesFromOffsets(ArrayList<ChessMove> moves, int[][] moveOffsets, ChessPosition start) {
        int row = start.getRow();
        int col = start.getColumn();

        for (int[] offset : moveOffsets) {
            int newRow = row + offset[0];
            int newCol = col + offset[1];
            if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8) {
                ChessPosition end = new ChessPosition(newRow, newCol);
                if (board.hasPiece(end)) {
                    if (board.getPiece(end).getTeamColor() != color) {
                        moves.add(new ChessMove(start, end));
                    }
                } else {
                    moves.add(new ChessMove(start, end));
                }
            }
        }
    }

    public ArrayList<ChessMove> pawnMoves(ChessGame.TeamColor color) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        int direction = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (color == ChessGame.TeamColor.WHITE) ? 8 : 1;

        ChessPosition end = new ChessPosition(row + direction, col);
        ChessPosition longEnd = new ChessPosition(row + 2 * direction, col);
        ChessPosition atkLeft = new ChessPosition(row + direction, col - 1);
        ChessPosition atkRight = new ChessPosition(row + direction, col + 1);

        if (row + direction == promotionRow) {
            handlePromotionMoves(moves, start, end, atkLeft, atkRight);
        } else if (row == startRow) {
            handleInitialPawnMoves(moves, start, end, longEnd, atkLeft, atkRight);
        } else {
            handleNormalPawnMoves(moves, start, end, atkLeft, atkRight);
        }

        return moves;
    }

    private void handlePromotionMoves(ArrayList<ChessMove> moves, ChessPosition start, ChessPosition end, ChessPosition atkLeft, ChessPosition atkRight) {
        if (!board.hasPiece(end)) {
            addPromotionMoves(moves, start, end);
        }
        if (board.hasPiece(atkLeft) && board.getPiece(atkLeft).getTeamColor() != color) {
            addPromotionMoves(moves, start, atkLeft);
        }
        if (board.hasPiece(atkRight) && board.getPiece(atkRight).getTeamColor() != color) {
            addPromotionMoves(moves, start, atkRight);
        }
    }

    private void handleInitialPawnMoves(ArrayList<ChessMove> moves, ChessPosition start, ChessPosition end, ChessPosition longEnd, ChessPosition atkLeft, ChessPosition atkRight) {
        if (!board.hasPiece(end)) {
            moves.add(new ChessMove(start, end));
            if (!board.hasPiece(longEnd)) {
                moves.add(new ChessMove(start, longEnd));
            }
        }
        addCaptureMoves(moves, start, atkLeft, color);
        addCaptureMoves(moves, start, atkRight, color);
    }

    private void handleNormalPawnMoves(ArrayList<ChessMove> moves, ChessPosition start, ChessPosition end, ChessPosition atkLeft, ChessPosition atkRight) {
        if (!board.hasPiece(end)) {
            moves.add(new ChessMove(start, end));
        }
        addCaptureMoves(moves, start, atkLeft, color);
        addCaptureMoves(moves, start, atkRight, color);
    }

    private void addPromotionMoves(ArrayList<ChessMove> moves, ChessPosition start, ChessPosition end) {
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
    }

    private void addCaptureMoves(ArrayList<ChessMove> moves, ChessPosition start, ChessPosition end, ChessGame.TeamColor color) {
        if (board.hasPiece(end) && board.getPiece(end).getTeamColor() != color) {
            moves.add(new ChessMove(start, end));
        }
    }

}

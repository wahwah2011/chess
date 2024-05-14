package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }



    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN;
    }
    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     *
     *
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        if(this.type == PieceType.BISHOP) {
            BishopMovesCalculator calc = new BishopMovesCalculator(board,position,pieceColor);
            moves = calc.bishopMoves();
        }
        else if (this.type == PieceType.ROOK) {
            RookMovesCalculator calc = new RookMovesCalculator(board,position,pieceColor);
            moves = calc.rookMoves();
        }
        else if (this.type == PieceType.QUEEN) {
            QueenMovesCalculator calc = new QueenMovesCalculator(board,position,pieceColor);
            moves = calc.queenMoves();
        }
        else if (this.type == PieceType.KING) {
            KingMovesCalculator calc = new KingMovesCalculator(board,position,pieceColor);
            moves = calc.kingMoves();
        }
        else if (this.type == PieceType.KNIGHT) {
            KnightMovesCalculator calc = new KnightMovesCalculator(board,position,pieceColor);
            moves = calc.knightMoves();
        }
        else if (this.type == PieceType.PAWN) {
            PawnMovesCalculator calc = new PawnMovesCalculator(board,position,pieceColor);
            moves = calc.pawnMoves();
        }

        return moves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public ChessPiece clone() {
        ChessPiece clonePiece = new ChessPiece(this.pieceColor,this.type);
        return clonePiece;
    }
}

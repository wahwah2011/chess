package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;
    private ChessGame.TeamColor color;

    public BishopMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        this.board = board;
        this.position = position;
        this.color = color;
    }

    public Collection<ChessMove> bishopMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(moveDiagonal());
        return moves;
    }

    public ArrayList<ChessMove> moveDiagonal() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        moves.addAll(upRight());
        moves.addAll(upLeft());
        moves.addAll(downLeft());
        moves.addAll(downRight());

        return moves;
    }

    public ArrayList<ChessMove> upRight() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int curRow = position.getRow();
        int curCol = position.getColumn();

        while (curRow < 8 && curCol < 8) { // just less than 8 because if it is 8, there is nothign to check beyond
            ChessPosition end = new ChessPosition(curRow + 1,curCol + 1);
            if (board.hasPiece(end)) {
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
                break;
            }
            ChessMove newMove = new ChessMove(start,end);
            moves.add(newMove);
            curRow++;
            curCol++;
        }

        System.out.print(moves);
        return moves;
    }

    public ArrayList<ChessMove> upLeft() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int curRow = position.getRow();
        int curCol = position.getColumn();

        while (curRow < 8 && curCol > 1) { // just less than 8 because if it is 8, there is nothign to check beyond
            ChessPosition end = new ChessPosition(curRow + 1,curCol - 1);
            if (board.hasPiece(end)) {
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
                break;
            }
            ChessMove newMove = new ChessMove(start,end);
            moves.add(newMove);
            curRow++;
            curCol--;
        }

        System.out.print(moves);
        return moves;
    }

    public ArrayList<ChessMove> downLeft() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int curRow = position.getRow();
        int curCol = position.getColumn();

        while (curRow > 1 && curCol > 1) { // just greater than 1 because if it is 1, there is nothign to check beyond
            ChessPosition end = new ChessPosition(curRow - 1,curCol - 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
                break;
            }
            ChessMove newMove = new ChessMove(start,end);
            moves.add(newMove);
            curRow--;
            curCol--;
        }

        System.out.print(moves);
        return moves;
    }

    public ArrayList<ChessMove> downRight() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int curRow = position.getRow();
        int curCol = position.getColumn();

        while (curRow > 1 && curCol < 8) { // just greater than 1 because if it is 1, there is nothign to check beyond
            ChessPosition end = new ChessPosition(curRow - 1,curCol + 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
                break;
            }
            ChessMove newMove = new ChessMove(start,end);
            moves.add(newMove);
            curRow--;
            curCol++;
        }

        System.out.print(moves);
        return moves;
    }
}
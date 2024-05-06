package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;
    private ChessGame.TeamColor color;

    public QueenMovesCalculator(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        this.board = board;
        this.position = position;
        this.color = color;
    }

    public Collection<ChessMove> queenMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(moveHorizontal());
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

    public ArrayList<ChessMove> moveHorizontal() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(leftRight());
        moves.addAll(upDown());
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

    public ArrayList<ChessMove> upDown() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int upRow = position.getRow();
        int downRow = position.getRow();
        int curCol = position.getColumn();

        while (upRow < 8) { // just less than 8 because if it is 8, there is nothign to check beyond
            ChessPosition upEnd = new ChessPosition(upRow + 1,curCol);
            if (board.hasPiece(upEnd)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(upEnd);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,upEnd);
                    moves.add(newMove);
                }
                break;
            }
            ChessMove newMove = new ChessMove(start,upEnd);
            moves.add(newMove);
            upRow++;
        }

        while (downRow > 1) { // just less than 8 because if it is 8, there is nothign to check beyond
            ChessPosition downEnd = new ChessPosition(downRow - 1, curCol);
            if (board.hasPiece(downEnd)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(downEnd);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,downEnd);
                    moves.add(newMove);
                }
                break;
            }
            ChessMove newMove = new ChessMove(start,downEnd);
            moves.add(newMove);
            downRow--;
        }

        System.out.print(moves);
        return moves;
    }

    public ArrayList<ChessMove> leftRight() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int curRow = position.getRow();
        int leftCol = position.getColumn();
        int rightCol = position.getColumn();

        while (rightCol < 8) { // just less than 8 because if it is 8, there is nothign to check beyond
            ChessPosition rightEnd = new ChessPosition(curRow,rightCol + 1);
            if (board.hasPiece(rightEnd)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(rightEnd);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,rightEnd);
                    moves.add(newMove);
                }
                break;
            }
            ChessMove newMove = new ChessMove(start,rightEnd);
            moves.add(newMove);
            rightCol++;
        }

        while (leftCol > 1) { // just less than 8 because if it is 8, there is nothign to check beyond
            ChessPosition leftEnd = new ChessPosition(curRow, leftCol - 1);
            if (board.hasPiece(leftEnd)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(leftEnd);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,leftEnd);
                    moves.add(newMove);
                }
                break;
            }
            ChessMove newMove = new ChessMove(start,leftEnd);
            moves.add(newMove);
            leftCol--;
        }

        System.out.print(moves);
        return moves;
    }
}

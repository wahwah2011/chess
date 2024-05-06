package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator {

    private ChessBoard board;
    private ChessPosition position;

    public RookMovesCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> rookMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(moveHorizontal());
        return moves;
    }


    public ArrayList<ChessMove> moveHorizontal() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        moves.addAll(leftRight());
        moves.addAll(upDown());
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
            ChessPosition upEnd = new ChessPosition(curRow,rightCol + 1);
            if (board.hasPiece(upEnd)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                break;
            }
            ChessMove newMove = new ChessMove(start,upEnd);
            moves.add(newMove);
            rightCol++;
        }

        while (leftCol > 1) { // just less than 8 because if it is 8, there is nothign to check beyond
            ChessPosition downEnd = new ChessPosition(curRow, leftCol - 1);
            if (board.hasPiece(downEnd)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                break;
            }
            ChessMove newMove = new ChessMove(start,downEnd);
            moves.add(newMove);
            leftCol--;
        }

        System.out.print(moves);
        return moves;
    }
}

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

    public ArrayList<ChessMove> kMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int curRow = position.getRow();
        int curCol = position.getColumn();

        //case 1: bottom-left corner
        if (curRow > 1 && curCol > 1) {
            ChessPosition end = new ChessPosition(curRow - 1,curCol - 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 2: bottom-right corner
        if (curRow > 1 && curCol < 8) {
            ChessPosition end = new ChessPosition(curRow - 1,curCol + 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 3: top-right corner
        if (curRow < 8 && curCol < 8) {
            ChessPosition end = new ChessPosition(curRow + 1,curCol + 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 4: top-left corner
        if (curRow < 8 && curCol > 1) {
            ChessPosition end = new ChessPosition(curRow + 1,curCol - 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 5: middle-left
        if (curCol > 1) {
            ChessPosition end = new ChessPosition(curRow,curCol - 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 6: middle-right
        if (curCol < 8) {
            ChessPosition end = new ChessPosition(curRow,curCol + 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 7: middle-bottom
        if (curRow > 1) {
            ChessPosition end = new ChessPosition(curRow - 1,curCol);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 8: middle-top
        if (curRow < 8) {
            ChessPosition end = new ChessPosition(curRow + 1,curCol);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        return moves;
    }

    public ArrayList<ChessMove> nMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int curRow = position.getRow();
        int curCol = position.getColumn();

        //case 1: top row left
        if (curRow < 7 && curCol > 1) {
            ChessPosition end = new ChessPosition(curRow + 2,curCol - 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 2: top row right
        if (curRow < 7 && curCol < 8) {
            ChessPosition end = new ChessPosition(curRow + 2,curCol + 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 3: middle top row left
        if (curRow < 8 && curCol > 2) {
            ChessPosition end = new ChessPosition(curRow + 1,curCol - 2);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 4: middle top row right
        if (curRow < 8 && curCol < 7) {
            ChessPosition end = new ChessPosition(curRow + 1,curCol + 2);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 5: bottom row left
        if (curRow > 2 && curCol > 1) {
            ChessPosition end = new ChessPosition(curRow - 2,curCol - 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 6: bottom row right
        if (curRow > 2 && curCol < 8) {
            ChessPosition end = new ChessPosition(curRow - 2,curCol + 1);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 7: middle bot row left
        if (curRow > 1 && curCol > 2) {
            ChessPosition end = new ChessPosition(curRow - 1,curCol - 2);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        //case 8: middle bot row right
        if (curRow > 1 && curCol < 7) {
            ChessPosition end = new ChessPosition(curRow - 1,curCol + 2);
            if (board.hasPiece(end)) {
                //IF OTHER COLOR, THEN CAN ADVANCE TO THAT SPACE//CAPTURE THE ENEMY
                ChessPiece endPiece = board.getPiece(end);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,end);
                    moves.add(newMove);
                }
            }
            else {
                ChessMove newMove = new ChessMove(start,end);
                moves.add(newMove);
            }
        }

        return moves;
    }

    public ArrayList<ChessMove> whitePMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int curRow = position.getRow();
        int curCol = position.getColumn();

        ChessPosition defaultEnd = new ChessPosition(curRow + 1,curCol);
        ChessPosition longEnd = new ChessPosition(curRow + 2,curCol);
        ChessPosition leftAttack = new ChessPosition(curRow + 1,curCol - 1);
        ChessPosition rightAttack = new ChessPosition(curRow + 1,curCol + 1);

        if (curRow + 1 == 8) {
            if (!board.hasPiece(defaultEnd)) {
                moves.add(new ChessMove(start, defaultEnd, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(start, defaultEnd, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(start, defaultEnd, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(start, defaultEnd, ChessPiece.PieceType.KNIGHT));
            }
            if (board.hasPiece(leftAttack)) {
                ChessPiece endPiece = board.getPiece(leftAttack);
                if (endPiece.getTeamColor() != this.color) {
                    moves.add(new ChessMove(start, leftAttack, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start, leftAttack, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start, leftAttack, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(start, leftAttack, ChessPiece.PieceType.KNIGHT));
                }
            }
            if (board.hasPiece(rightAttack)) {
                ChessPiece endPiece = board.getPiece(rightAttack);
                if (endPiece.getTeamColor() != this.color) {
                    moves.add(new ChessMove(start, rightAttack, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start, rightAttack, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start, rightAttack, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(start, rightAttack, ChessPiece.PieceType.KNIGHT));
                }
            }
            /*validMoves.add(new ChessMove(startingPosition, end, ChessPiece.PieceType.QUEEN));
            validMoves.add(new ChessMove(startingPosition, end, ChessPiece.PieceType.BISHOP));
            validMoves.add(new ChessMove(startingPosition, end, ChessPiece.PieceType.ROOK));
            validMoves.add(new ChessMove(startingPosition, end, ChessPiece.PieceType.KNIGHT));*/
        }
        else if (curRow == 2) {
            if (!board.hasPiece(defaultEnd)) {
                ChessMove newMove = new ChessMove(start, defaultEnd);
                moves.add(newMove);
                if (!board.hasPiece(longEnd)) {
                    ChessMove otherMove = new ChessMove(start, longEnd);
                    moves.add(otherMove);
                }
            }
        }
        else {
            if (!board.hasPiece(defaultEnd)) {
                ChessMove newMove = new ChessMove(start, defaultEnd);
                moves.add(newMove);
            }
            if (board.hasPiece(leftAttack)) {
                ChessPiece endPiece = board.getPiece(leftAttack);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,leftAttack);
                    moves.add(newMove);
                }
            }
            if (board.hasPiece(rightAttack)) {
                ChessPiece endPiece = board.getPiece(rightAttack);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,rightAttack);
                    moves.add(newMove);
                }
            }
        }

        return moves;
    }

    public ArrayList<ChessMove> blackPMoves() {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition start = new ChessPosition(position.getRow(),position.getColumn());
        int curRow = position.getRow();
        int curCol = position.getColumn();

        ChessPosition defaultEnd = new ChessPosition(curRow - 1,curCol);
        ChessPosition longEnd = new ChessPosition(curRow - 2,curCol);
        ChessPosition leftAttack = new ChessPosition(curRow - 1,curCol - 1);
        ChessPosition rightAttack = new ChessPosition(curRow - 1,curCol + 1);

        if (curRow - 1 == 1) {
            if (!board.hasPiece(defaultEnd)) {
                moves.add(new ChessMove(start, defaultEnd, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(start, defaultEnd, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(start, defaultEnd, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(start, defaultEnd, ChessPiece.PieceType.KNIGHT));
            }
            if (board.hasPiece(leftAttack)) {
                ChessPiece endPiece = board.getPiece(leftAttack);
                if (endPiece.getTeamColor() != this.color) {
                    moves.add(new ChessMove(start, leftAttack, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start, leftAttack, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start, leftAttack, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(start, leftAttack, ChessPiece.PieceType.KNIGHT));
                }
            }
            if (board.hasPiece(rightAttack)) {
                ChessPiece endPiece = board.getPiece(rightAttack);
                if (endPiece.getTeamColor() != this.color) {
                    moves.add(new ChessMove(start, rightAttack, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start, rightAttack, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start, rightAttack, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(start, rightAttack, ChessPiece.PieceType.KNIGHT));
                }
            }
        }
        else if (curRow == 7) {
            if (!board.hasPiece(defaultEnd)) {
                ChessMove newMove = new ChessMove(start, defaultEnd);
                moves.add(newMove);
                if (!board.hasPiece(longEnd)) {
                    ChessMove otherMove = new ChessMove(start, longEnd);
                    moves.add(otherMove);
                }
            }

        }
        else {
            if (!board.hasPiece(defaultEnd)) {
                ChessMove newMove = new ChessMove(start, defaultEnd);
                moves.add(newMove);
            }
            if (board.hasPiece(leftAttack)) {
                ChessPiece endPiece = board.getPiece(leftAttack);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,leftAttack);
                    moves.add(newMove);
                }
            }
            if (board.hasPiece(rightAttack)) {
                ChessPiece endPiece = board.getPiece(rightAttack);
                if (endPiece.getTeamColor() != this.color) {
                    ChessMove newMove = new ChessMove(start,rightAttack);
                    moves.add(newMove);
                }
            }
        }


        return moves;
    }
}

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

    public ArrayList<ChessMove> diagonal() {
        ArrayList<ChessMove> moves = new ArrayList<>();

        moves.addAll(upRight());
        moves.addAll(upLeft());
        moves.addAll(downRight());
        moves.addAll(downLeft());

        return moves;
    }

    public ArrayList<ChessMove> horizontal() {
        ArrayList<ChessMove> moves = new ArrayList<>();

        moves.addAll(upDown());
        moves.addAll(leftRight());

        return moves;
    }

    public ArrayList<ChessMove> upDown() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        while (row < 8) {
            ChessPosition end = new ChessPosition(row + 1, col);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
                break;
            }
            else if (!board.hasPiece(end)){
                moves.add(new ChessMove(start,end));
            }

            row++;
        }

        row = position.getRow();
        col = position.getColumn();

        while (row > 1) {
            ChessPosition end = new ChessPosition(row - 1, col);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
                break;
            }
            else if (!board.hasPiece(end)){
                moves.add(new ChessMove(start,end));
            }

            row--;
        }

        return moves;
    }

    public ArrayList<ChessMove> leftRight() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row,col);

        while (col < 8) {
            ChessPosition end = new ChessPosition(row, col + 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
                break;
            }
            else if (!board.hasPiece(end)){
                moves.add(new ChessMove(start,end));
            }

            col++;
        }

        row = position.getRow();
        col = position.getColumn();

        while (col > 1) {
            ChessPosition end = new ChessPosition(row, col - 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
                break;
            }
            else if (!board.hasPiece(end)){
                moves.add(new ChessMove(start,end));
            }

            col--;
        }

        return moves;
    }

    public ArrayList<ChessMove> upRight() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        while (row < 8 && col < 8) {
            ChessPosition end = new ChessPosition(row + 1, col + 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
                break;
            }
            else if (!board.hasPiece(end)){
                moves.add(new ChessMove(start,end));
            }

            row++;
            col++;
        }

        return moves;
    }

    public ArrayList<ChessMove> upLeft() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        while (row < 8 && col > 1) {
            ChessPosition end = new ChessPosition(row + 1, col - 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
                break;
            }
            else {
                moves.add(new ChessMove(start,end));
            }

            row++;
            col--;
        }

        return moves;
    }

    public ArrayList<ChessMove> downRight() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        while (row > 1 && col < 8) {
            ChessPosition end = new ChessPosition(row - 1, col + 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
                break;
            }
            else {
                moves.add(new ChessMove(start,end));
            }

            row--;
            col++;
        }

        return moves;
    }

    public ArrayList<ChessMove> downLeft() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        while (row > 1 && col > 1) {
            ChessPosition end = new ChessPosition(row - 1, col - 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
                break;
            }
            else {
                moves.add(new ChessMove(start,end));
            }

            row--;
            col--;
        }

        return moves;
    }

    public ArrayList<ChessMove> kingMove() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        //case 1: top right move
        if (row < 8 && col < 8) {
            ChessPosition end = new ChessPosition(row + 1, col + 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 2: top left move
        if (row < 8 && col > 1) {
            ChessPosition end = new ChessPosition(row + 1, col - 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 3: bot left move
        if (row > 1 && col > 1) {
            ChessPosition end = new ChessPosition(row - 1, col - 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 4: bot right move
        if (row > 1 && col < 8) {
            ChessPosition end = new ChessPosition(row - 1, col + 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 5: top mid move
        if (row < 8) {
            ChessPosition end = new ChessPosition(row + 1, col);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 6: top mid move
        if (row > 1) {
            ChessPosition end = new ChessPosition(row - 1, col);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 7: right mid move
        if (col < 8) {
            ChessPosition end = new ChessPosition(row, col + 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 8: right mid move
        if (col > 1) {
            ChessPosition end = new ChessPosition(row, col - 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        return moves;
    }

    public ArrayList<ChessMove> knightMove() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        //case 1: top row right
        if (row < 7 && col < 8) {
            ChessPosition end = new ChessPosition(row + 2, col + 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 2: top row left
        if (row < 7 && col > 1) {
            ChessPosition end = new ChessPosition(row + 2, col - 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 3: mid-top row right
        if (row < 8 && col < 7) {
            ChessPosition end = new ChessPosition(row + 1, col + 2);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 4: mid-top row left
        if (row < 8 && col > 2) {
            ChessPosition end = new ChessPosition(row + 1, col - 2);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 5: mid-bot row left
        if (row > 1 && col > 2) {
            ChessPosition end = new ChessPosition(row - 1, col - 2);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 6: mid-bot row right
        if (row > 1 && col < 7) {
            ChessPosition end = new ChessPosition(row - 1, col + 2);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 7: bot row right
        if (row > 2 && col < 8) {
            ChessPosition end = new ChessPosition(row - 2, col + 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        //case 8: bot row left
        if (row > 2 && col > 1) {
            ChessPosition end = new ChessPosition(row - 2, col - 1);
            if (board.hasPiece(end)) {
                if (board.getPiece(end).getTeamColor() != color) {
                    moves.add(new ChessMove(start,end));
                }
            }
            else {
                moves.add(new ChessMove(start,end));
            }
        }

        return moves;
    }

    public ArrayList<ChessMove> whitePawnMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        ChessPosition end = new ChessPosition(row + 1, col);
        ChessPosition longEnd = new ChessPosition(row + 2, col);
        ChessPosition atkLeft = new ChessPosition(row + 1, col - 1);
        ChessPosition atkRight = new ChessPosition(row + 1, col + 1);

        //if (row + 1 == 8) promotion :)
        if (end.getRow() == 8) {
            if (!board.hasPiece(end)) {
                moves.add(new ChessMove(start,end, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(start,end, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(start,end, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(start,end, ChessPiece.PieceType.ROOK));
            }
            if (board.hasPiece(atkLeft)) {
                if (board.getPiece(atkLeft).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkLeft, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start,atkLeft, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(start,atkLeft, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start,atkLeft, ChessPiece.PieceType.ROOK));
                }
            }
            if (board.hasPiece(atkRight)) {
                if (board.getPiece(atkRight).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkRight, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start,atkRight, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(start,atkRight, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start,atkRight, ChessPiece.PieceType.ROOK));
                }
            }
        }
        //if (row == 2) extended advance
        else if (start.getRow() == 2) {
            if (!board.hasPiece(end)) {
                moves.add(new ChessMove(start,end));
                if (!board.hasPiece(longEnd)) {
                    moves.add(new ChessMove(start,longEnd));
                }
            }
            if (board.hasPiece(atkLeft)) {
                if (board.getPiece(atkLeft).getTeamColor() != color) {
                   moves.add(new ChessMove(start,atkLeft));
                }
            }
            if (board.hasPiece(atkRight)) {
                if (board.getPiece(atkRight).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkRight));
                }
            }
        }

        //else normal move
        else {
            if (!board.hasPiece(end)) {
                moves.add(new ChessMove(start,end));
            }
            if (board.hasPiece(atkLeft)) {
                if (board.getPiece(atkLeft).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkLeft));
                }
            }
            if (board.hasPiece(atkRight)) {
                if (board.getPiece(atkRight).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkRight));
                }
            }
        }

        return moves;
    }

    public ArrayList<ChessMove> blackPawnMoves() {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);

        ChessPosition end = new ChessPosition(row - 1, col);
        ChessPosition longEnd = new ChessPosition(row - 2, col);
        ChessPosition atkLeft = new ChessPosition(row - 1, col - 1);
        ChessPosition atkRight = new ChessPosition(row - 1, col + 1);

        //if (row - 1 == 1) promotion :)
        if (end.getRow() == 1) {
            if (!board.hasPiece(end)) {
                moves.add(new ChessMove(start,end, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(start,end, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(start,end, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(start,end, ChessPiece.PieceType.ROOK));
            }
            if (board.hasPiece(atkLeft)) {
                if (board.getPiece(atkLeft).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkLeft, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start,atkLeft, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(start,atkLeft, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start,atkLeft, ChessPiece.PieceType.ROOK));
                }
            }
            if (board.hasPiece(atkRight)) {
                if (board.getPiece(atkRight).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkRight, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start,atkRight, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(start,atkRight, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start,atkRight, ChessPiece.PieceType.ROOK));
                }
            }
        }
        //if (row == 7) extended advance
        else if (start.getRow() == 7) {
            if (!board.hasPiece(end)) {
                moves.add(new ChessMove(start,end));
                if (!board.hasPiece(longEnd)) {
                    moves.add(new ChessMove(start,longEnd));
                }
            }
            if (board.hasPiece(atkLeft)) {
                if (board.getPiece(atkLeft).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkLeft));
                }
            }
            if (board.hasPiece(atkRight)) {
                if (board.getPiece(atkRight).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkRight));
                }
            }
        }

        //else normal move
        else {
            if (!board.hasPiece(end)) {
                moves.add(new ChessMove(start,end));
            }
            if (board.hasPiece(atkLeft)) {
                if (board.getPiece(atkLeft).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkLeft));
                }
            }
            if (board.hasPiece(atkRight)) {
                if (board.getPiece(atkRight).getTeamColor() != color) {
                    moves.add(new ChessMove(start,atkRight));
                }
            }
        }

        return moves;
    }


}

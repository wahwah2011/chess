 package ui.chessboard;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private ChessBoard chessBoard; //8x8 2D array which contains ChessPiece objects
    private boolean[][] validMoves = new boolean[8][8];
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;

    public static void main(String[] args) {
        DrawBoard drawBoard = new DrawBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        drawBoard.drawValidMoveBoard(new ChessPosition(8,7), out, "black");
    }

    public DrawBoard() {
        this.chessBoard = new ChessBoard();
        /*chessBoard.addPiece(new ChessPosition(5,5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        chessBoard.addPiece(new ChessPosition(6,6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        chessBoard.addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        chessBoard.addPiece(new ChessPosition(1,8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        chessBoard.addPiece(new ChessPosition(8,1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        chessBoard.addPiece(new ChessPosition(8,8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));*/
        this.chessBoard.resetBoard();
    }

    public DrawBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public void drawValidMoveBoard(ChessPosition position, PrintStream out, String playerColor) {
        if (containsPiece(position)) {
            if (sameColor(position, playerColor)) {
                validMoves = calculateValidMoves(position);
                drawChessBoard(out, playerColor);
                resetValidMoves(validMoves);
            }
            else out.println("That is not your piece, and therefore shouldn't concern you.");
        }
        else out.println("That is not a valid piece position.");
    }

    public boolean[][] calculateValidMoves(ChessPosition position) {
        boolean[][] trueMoves = new boolean[8][8];
        ChessPiece curPiece = chessBoard.getPiece(position);
        ArrayList<ChessMove> validMoves = (ArrayList<ChessMove>) curPiece.pieceMoves(chessBoard,position);
        for (ChessMove curMove : validMoves) {
            int row = curMove.getEndPosition().getRow();
            int col = curMove.getEndPosition().getColumn();
            trueMoves[row - 1][col - 1] = true;
        }
        return trueMoves;
    }

    public void drawChessBoard(PrintStream out, String playerColor) {
        drawHeaders(out, playerColor);
        drawRowSquares(out, playerColor);
        drawHeaders(out, playerColor);
    }

    public void drawObserverView(PrintStream out) {
        drawChessBoard(out, "white");

        out.print(SET_BG_COLOR_BLACK);
        out.print(EMPTY.repeat(10));
        out.print(RESET_BG_COLOR);
        out.print('\n');

        drawChessBoard(out, "black");
    }

    private void drawHeaders(PrintStream out, String playerColor) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);
        String[] headers = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
        String[] reverseHeaders = {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};
        if (playerColor.equals("black")) {
            printHeaderText(out, reverseHeaders);
        } else {
            printHeaderText(out, headers);
        }
        out.print(RESET_TEXT_BOLD_FAINT);
    }

    private void printHeaderText(PrintStream out, String[] headerText) {
        for (String header : headerText) {
            out.print(header);
        }
        out.print(RESET_BG_COLOR);
        out.print("\n");
    }

    private void drawRowSquares(PrintStream out, String playerColor) {
        int[] rowNumbers = {1, 2, 3, 4, 5, 6, 7, 8};
        //int[] revRowNumbers = {8, 7, 6, 5, 4, 3, 2, 1};

        if (playerColor.equals("white")) {
            drawRevRows(out, rowNumbers);
        } else {
            drawRows(out, rowNumbers);
        }
    }

    private void drawRows(PrintStream out, int[] rowNumbers) {
        for (int i = 0; i < rowNumbers.length; i++) {
            drawRowNum(out, rowNumbers[i]);
            for (int col = BOARD_SIZE_IN_SQUARES - 1; col >= 0; col--) {
                drawSquare(out, rowNumbers[i], col);
            }
            drawRowNum(out, rowNumbers[i]);
            out.print("\n");
        }
    }

    private void drawRevRows(PrintStream out, int[] rowNumbers) {
        for (int i = rowNumbers.length - 1; i >= 0; i--) {
            drawRowNum(out, rowNumbers[i]);
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
                drawSquare(out, rowNumbers[i], col);
            }
            drawRowNum(out, rowNumbers[i]);
            out.print("\n");
        }
    }

    private void drawRowNum(PrintStream out, int rowNum) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);
        out.print(" " + rowNum + " ");
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_BOLD_FAINT);
    }

    private void drawSquare(PrintStream out, int row, int col) {
        ChessPosition pos = new ChessPosition(row, col + 1);

        if (isValidMove(pos)) {
            if (isWhite(row, col)){
                out.print(SET_BG_COLOR_GREEN);
            }
            else out.print(SET_BG_COLOR_DARK_GREEN);
        }
        else if (isWhite(row, col)) {
            out.print(SET_BG_COLOR_WHITE);
        } else {
            out.print(SET_BG_COLOR_BLACK);
        }

        drawPiece(out, pos);
        out.print(RESET_BG_COLOR);
    }

    private void drawPiece(PrintStream out, ChessPosition pos) {
        String piece = "   ";
        String textColor = "";

        if (chessBoard.hasPiece(pos)) {
            piece = pieceToText(pos);
            textColor = teamTextColor(pos);
            out.print(textColor);
        }
        out.print(piece);
        out.print(RESET_TEXT_COLOR);
    }

    private boolean isWhite(int row, int col) {
        return (row + col) % 2 == 0;
    }

    private String pieceToText(ChessPosition pos) {
        ChessPiece currPiece = chessBoard.getPiece(pos);
        ChessPiece.PieceType type = currPiece.getPieceType();

        return switch (type) {
            case ROOK -> ROOK;
            case KNIGHT -> KNIGHT;
            case BISHOP -> BISHOP;
            case KING -> KING;
            case QUEEN -> QUEEN;
            case PAWN -> PAWN;
            default -> "   ";
        };
    }

    private String teamTextColor(ChessPosition pos) {
        ChessPiece currPiece = chessBoard.getPiece(pos);
        ChessGame.TeamColor color = currPiece.getTeamColor();

        if (color.equals(ChessGame.TeamColor.WHITE)) {
            return SET_TEXT_COLOR_RED;
        } else {
            return SET_TEXT_COLOR_BLUE;
        }
    }

    private void resetValidMoves(boolean[][] validMoves) {
        for (int i = 0; i < validMoves.length; i++) {
            for (int j = 0; j < validMoves[i].length; j++) {
                validMoves[i][j] = false;
            }
        }
    }

    private boolean isValidMove(ChessPosition pos) {
        int row = pos.getRow() - 1;
        int col = pos.getColumn() -1;
        return validMoves[row][col];
    }

    private boolean containsPiece(ChessPosition pos) {
        return chessBoard.hasPiece(pos);
    }

    private boolean sameColor(ChessPosition position, String playerColor) {
        ChessPiece curPiece = chessBoard.getPiece(position);
        ChessGame.TeamColor color;
        if (playerColor == "white") {
            color = ChessGame.TeamColor.WHITE;
        }
        else color = ChessGame.TeamColor.BLACK;

        return curPiece.getTeamColor() == color;
    }
}

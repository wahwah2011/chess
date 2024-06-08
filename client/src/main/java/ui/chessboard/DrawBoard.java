package ui.chessboard;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private ChessBoard chessBoard;
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;

    public static void main(String[] args) {
        DrawBoard drawBoard = new DrawBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawBoard.drawObserverView(out);
    }

    public DrawBoard() {
        this.chessBoard = new ChessBoard();
        chessBoard.resetBoard();
    }

    public DrawBoard(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
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

    @Override
    public String toString() {
        return "DrawBoard{" +
                "chessBoard=" + chessBoard.toString() +
                '}';
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

        if (isWhite(row, col)) {
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

        switch (type) {
            case ROOK:
                return ROOK;
            case KNIGHT:
                return KNIGHT;
            case BISHOP:
                return BISHOP;
            case KING:
                return KING;
            case QUEEN:
                return QUEEN;
            case PAWN:
                return PAWN;
            default:
                return "   ";
        }
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
}

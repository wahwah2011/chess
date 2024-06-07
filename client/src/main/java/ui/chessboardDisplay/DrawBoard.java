package ui.chessboardDisplay;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawBoard {
    //private DisplayBoard board;
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

    //add a non-default constructor that takes a DisplayBoard as input

    public void drawChessBoard(PrintStream out, String playerColor) {
        //keep track of team color; normal order if white, reverse if black
        drawHeaders(out,playerColor);
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
        //header or footer
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);
        String[] headers = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
        String[] reverseHeaders = {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};
        if (playerColor.equals("black")) {
            printHeaderText(out,headers);
        }
        else if (playerColor.equals("white")) {
            printHeaderText(out,reverseHeaders);
        }
        out.print(RESET_TEXT_BOLD_FAINT);
    }

    private void printHeaderText(PrintStream out, String[] headerText) {
        for (int boardCol = 0; boardCol < headerText.length; ++boardCol) {
            out.print(headerText[boardCol]);
        }
        out.print(RESET_BG_COLOR);
        out.print("\n");
    }

    private void drawRowSquares(PrintStream out, String playerColor) {
        int[] rowNumbers = {1, 2, 3, 4, 5, 6, 7, 8};
        int[] revRowNumbers = {8, 7, 6, 5, 4, 3, 2, 1};

        if (playerColor.equals("white")) {
            drawRows(out, revRowNumbers);
        }
        else if (playerColor.equals("black")) {
            drawRows(out, rowNumbers);
        }
    }

    private void drawRows(PrintStream out, int[] rowNumbers) {
        for (int row = 0; row < rowNumbers.length; row++) {
            drawRowNum(out, rowNumbers[row]);
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                drawSquare(out, rowNumbers[row], boardCol);
            }
            drawRowNum(out, rowNumbers[row]);
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
        ChessPosition pos = new ChessPosition(row,col + 1);

        if (isWhite(row,col)) {
            out.print(SET_BG_COLOR_WHITE);
        }
        else  {
            out.print(SET_BG_COLOR_BLACK);
        }

        drawPiece(out, pos);
    }

    private void drawPiece(PrintStream out, ChessPosition pos) {
        String piece = "   ";
        String textColor;

        if (chessBoard.hasPiece(pos)) {
            piece = pieceToText(pos);
            textColor = teamTextColor(pos);
            out.print(textColor);
        }
        out.print(piece);
    }

    private boolean isWhite(int row, int col) {
        return (row % 2 == col % 2);
    }

    private String pieceToText(ChessPosition pos) {
        ChessPiece currPiece = chessBoard.getPiece(pos);
        ChessPiece.PieceType type = currPiece.getPieceType();

        if (type.equals(ChessPiece.PieceType.ROOK)) {
            return ROOK;
        }
        else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
            return KNIGHT;
        }
        else if (type.equals(ChessPiece.PieceType.BISHOP)) {
            return BISHOP;
        }
        else  if (type.equals(ChessPiece.PieceType.KING)) {
            return KING;
        }
        else if (type.equals(ChessPiece.PieceType.QUEEN)) {
            return QUEEN;
        }
        else if (type.equals(ChessPiece.PieceType.PAWN)) {
            return PAWN;
        }
        else return null;
    }

    private String teamTextColor(ChessPosition pos) {
        ChessPiece currPiece = chessBoard.getPiece(pos);
        ChessGame.TeamColor color = currPiece.getTeamColor();

        if (color.equals(ChessGame.TeamColor.WHITE)) {
            return SET_TEXT_COLOR_BLUE;
        }
        else if (color.equals(ChessGame.TeamColor.BLACK)) {
            return SET_TEXT_COLOR_RED;
        }
        return SET_TEXT_COLOR_MAGENTA;
    }
}

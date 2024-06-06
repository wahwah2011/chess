package ui.chessboardDisplay;

import chess.ChessBoard;
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

        drawBoard.drawChessBoard(out);


    }

    public DrawBoard() {
        //default board
        /*ArrayList<DisplayPiece> pieces = new ArrayList<>();
        pieces.add(new DisplayPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)));
        pieces.add(new DisplayPiece(new ChessPosition(1,2),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT)));
        pieces.add(new DisplayPiece(new ChessPosition(1,3),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP)));
        pieces.add(new DisplayPiece(new ChessPosition(1,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN)));
        pieces.add(new DisplayPiece(new ChessPosition(1,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING)));
        pieces.add(new DisplayPiece(new ChessPosition(1,6),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP)));
        pieces.add(new DisplayPiece(new ChessPosition(1,7),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT)));
        pieces.add(new DisplayPiece(new ChessPosition(1,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)));

        pieces.add(new DisplayPiece(new ChessPosition(2,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(2,2),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(2,3),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(2,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(2,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(2,6),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(2,7),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(2,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)));

        pieces.add(new DisplayPiece(new ChessPosition(7,1),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(7,2),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(7,3),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(7,4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(7,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(7,6),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(7,7),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)));
        pieces.add(new DisplayPiece(new ChessPosition(7,8),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)));

        pieces.add(new DisplayPiece(new ChessPosition(8,1),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK)));
        pieces.add(new DisplayPiece(new ChessPosition(8,2),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT)));
        pieces.add(new DisplayPiece(new ChessPosition(8,3),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP)));
        pieces.add(new DisplayPiece(new ChessPosition(8,4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN)));
        pieces.add(new DisplayPiece(new ChessPosition(8,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING)));
        pieces.add(new DisplayPiece(new ChessPosition(8,6),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP)));
        pieces.add(new DisplayPiece(new ChessPosition(8,7),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT)));
        pieces.add(new DisplayPiece(new ChessPosition(8,8),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK)));
        this.board = new DisplayBoard(pieces);*/
        this.chessBoard = new ChessBoard();
        chessBoard.resetBoard();
    }

    //add a non-default constructor that takes a DisplayBoard as input

    public void drawChessBoard(PrintStream out) {
        //keep track of team color; normal order if white, reverse if black
        drawHeaders(out,"b");
        drawRowSquares(out);

        drawHeaders(out, "b");
    }

    private void drawHeaders(PrintStream out, String color) {
        //header or footer
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);
        String[] headers = {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
        String[] reverseHeaders = {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};
        if (color.equals("b")) {
            printHeaderText(out,headers);
        }
        else if (color.equals("w")) {
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

    private void drawRowSquares(PrintStream out) {
        int[] rowNumbers = {1, 2, 3, 4, 5, 6, 7, 8};
        for (int row = 0; row < rowNumbers.length; row++) {
            drawRowNum(out, rowNumbers[row]);
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                drawSquare(out, row, boardCol);
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
        ChessPosition pos = new ChessPosition(row + 1,col + 1);
        String piece = "   ";
        if (isWhite(row,col)) {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_BLACK);
        }
        else  {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_WHITE);
        }

        if (chessBoard.hasPiece(pos)) {
            piece = pieceToText(pos);
        }
        out.print(piece);
    }

    private void drawPlayer(PrintStream out) {

    }

    private ChessPiece.PieceType checkType(DisplayPiece piece) {
        return piece.piece().getPieceType();
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

    @Override
    public String toString() {
        return "DrawBoard{" +
                "chessBoard=" + chessBoard.toString() +
                '}';
    }
}

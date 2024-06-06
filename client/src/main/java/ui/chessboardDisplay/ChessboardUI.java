package ui.chessboardDisplay;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class ChessboardUI {

    private DisplayBoard board;

    public ChessboardUI() {
        //default board
        ArrayList<DisplayPiece> pieces = new ArrayList<>();
        pieces.add(new DisplayPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)));
        DisplayBoard newboard = new DisplayBoard(pieces);
    }

    //add a non-default constructor that takes a DisplayBoard as input

    public void drawChessBoard() {
        //keep track of
    }

    private void drawBorder() {
        //header or footer
    }

    private void drawRowSquares() {

    }

    private void drawSquare() {

    }
}

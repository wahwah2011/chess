package ui.chessboardDisplay;

import chess.ChessPiece;
import chess.ChessPosition;

public record DisplayPiece(ChessPosition position, ChessPiece piece) {
}

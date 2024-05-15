package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class BoardStats {
    private ChessGame game;

    public BoardStats(ChessGame game) {
        this.game = game;
    }

    public ChessPosition findKing(ChessGame.TeamColor teamColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>(boardPositions());
        ChessBoard gameBoard = game.getBoard();

        for (int i = positions.size() - 1; i >= 0; i--) {
            ChessPiece curPiece = game.getBoard().getPiece(positions.get(i));
            if (curPiece.getTeamColor().equals(teamColor) &&
                    curPiece.getPieceType().equals(ChessPiece.PieceType.KING)) {
                return positions.get(i);
            }
        }
        //SHOULD NEVER!
        return null;
    }

    //should work
    public Collection<ChessMove> friendlyMoves(ChessGame.TeamColor teamColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>(friendlyPositions(teamColor));
        ArrayList<ChessMove> frdMoves = new ArrayList<>();
        ChessBoard gameBoard = game.getBoard();

        //iterate through positions and find all possible moves for given gameboard
        for (ChessPosition pos : positions) {
            ChessPiece piece = gameBoard.getPiece(pos);
            frdMoves.addAll(piece.pieceMoves(gameBoard,pos));
        }

        return frdMoves;
    }

    //should work
    public Collection<ChessMove> enemyMoves(ChessGame.TeamColor teamColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>(enemyPositions(teamColor));
        ArrayList<ChessMove> oppMoves = new ArrayList<>();
        ChessBoard gameBoard = game.getBoard();


        //iterate through positions and find all possible moves for given gameboard
        for (ChessPosition pos : positions) {
            ChessPiece piece = gameBoard.getPiece(pos);
            oppMoves.addAll(piece.pieceMoves(gameBoard,pos));
        }

        return oppMoves;
    }

    //works
    public Collection<ChessPosition> enemyPositions(ChessGame.TeamColor teamColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>(boardPositions());
        ChessBoard gameBoard = game.getBoard();

        for (int i = positions.size() - 1; i >= 0; i--) {
            if (gameBoard.getPiece(positions.get(i)).getTeamColor().equals(teamColor)) {
                positions.remove(i);
            }
        }

        return positions;
    }

    //works
    public Collection<ChessPosition> friendlyPositions(ChessGame.TeamColor teamColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>(boardPositions());
        ChessBoard gameBoard = game.getBoard();

        for (int i = positions.size() - 1; i >= 0; i--) {
            if (!gameBoard.getPiece(positions.get(i)).getTeamColor().equals(teamColor)) {
                positions.remove(i);
            }
        }

        return positions;
    }

    //tested; works--maybe implement in board class?
    public Collection<ChessPosition> boardPositions() {
        ArrayList<ChessPosition> positions = new ArrayList<>();
        ChessBoard gameBoard = game.getBoard();

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i,j);
                if (gameBoard.hasPiece(pos)) {
                    positions.add(pos);
                }
            }
        }
        return positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardStats that = (BoardStats) o;
        return Objects.equals(game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(game);
    }
}

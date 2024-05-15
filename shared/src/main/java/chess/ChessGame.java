package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard gameBoard;
    private TeamColor teamTurn;

    public ChessGame() {
    teamTurn = TeamColor.WHITE;
    gameBoard = new ChessBoard();
    gameBoard.resetBoard();
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        ArrayList<ChessPosition> pos = new ArrayList<>(game.enemyPositions(TeamColor.WHITE));
        ArrayList<ChessMove> moves = new ArrayList<>(game.enemyMoves(TeamColor.WHITE));
        boolean isCheck = game.isInCheck(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board.clone();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
        }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece startPiece = gameBoard.getPiece(move.getStartPosition());
        ChessPosition endPos = move.getEndPosition();
        boolean hasEnd = false;
        ArrayList<ChessMove> possMoves = new ArrayList<>(startPiece.pieceMoves(gameBoard, move.getStartPosition()));
        for (ChessMove m : possMoves) {
            if (m.getEndPosition().equals(endPos)) {
                hasEnd = true;
            }
        }
        //if cannot move there
        if (!hasEnd) {
            throw new InvalidMoveException("The piece cannot move to the desired end position");
        }

        //elif leaves king in danger
        //STILL NEED TO IMPLEMENT THIS CASE

        //elif move.startposition.piece.getteam != teamTurn
        else if (startPiece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("It is not the appropriate team's turn");
        }
        //else
        else {
            gameBoard.addPiece(endPos,startPiece);
            gameBoard.clearPosition(move.getStartPosition());
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ArrayList<ChessMove> oppMoves = new ArrayList<>(enemyMoves(teamColor));
        ChessPosition kingPos = findKing(teamColor);
        boolean isCheck = false;

        for (ChessMove move : oppMoves) {
            ChessPosition endPos = move.getEndPosition();
            if (endPos.equals(kingPos)) {
                isCheck = true;
            }
        }

        return isCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    //POTENTIALLY COULD IMPLEMENT A PIECEFINDER CLASS???
    public ChessPosition findKing(TeamColor teamColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>(boardPositions());

        for (int i = positions.size() - 1; i >= 0; i--) {
            ChessPiece curPiece = gameBoard.getPiece(positions.get(i));
            if (curPiece.getTeamColor().equals(teamColor) &&
                    curPiece.getPieceType().equals(ChessPiece.PieceType.KING)) {
                return positions.get(i);
            }
        }
        //SHOULD NEVER!
        return null;
    }

    //should work
    public Collection<ChessMove> enemyMoves(TeamColor teamColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>(enemyPositions(teamColor));
        ArrayList<ChessMove> oppMoves = new ArrayList<>();

        //iterate through positions and find all possible moves for given gameboard
        for (ChessPosition pos : positions) {
            ChessPiece piece = gameBoard.getPiece(pos);
            oppMoves.addAll(piece.pieceMoves(gameBoard,pos));
        }

        return oppMoves;
    }

    //works
    public Collection<ChessPosition> enemyPositions(TeamColor teamColor) {
        ArrayList<ChessPosition> positions = new ArrayList<>(boardPositions());

        for (int i = positions.size() - 1; i >= 0; i--) {
            if (gameBoard.getPiece(positions.get(i)).getTeamColor().equals(teamColor)) {
                positions.remove(i);
            }
        }

        return positions;
    }

    //tested; works--maybe implement in board class?
    public Collection<ChessPosition> boardPositions() {
        ArrayList<ChessPosition> positions = new ArrayList<>();
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
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameBoard, teamTurn);
    }
}

package chess;

import java.util.*;

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
    gameBoard = new ChessBoard();
    gameBoard.resetBoard();
    teamTurn = TeamColor.WHITE;
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        ChessGame cloneGame = game.clone();
        cloneGame.getBoard().addPiece(new ChessPosition(5,5),new ChessPiece(TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        /*ArrayList<ChessPosition> pos = new ArrayList<>(game.enemyPositions(TeamColor.WHITE));
        ArrayList<ChessMove> moves = new ArrayList<>(game.validMoves(new ChessPosition(2,1)));
        ArrayList<ChessMove> vMoves = new ArrayList<>(game.validMoves(new ChessPosition(1,1)));*/
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
        ChessPiece piece = gameBoard.getPiece(startPosition);
        TeamColor color = piece.getTeamColor();

        Set<ChessMove> moves = new HashSet<>(piece.pieceMoves(gameBoard, startPosition));

        // Create an iterator to safely remove elements while iterating
        Iterator<ChessMove> iterator = moves.iterator();
        while (iterator.hasNext()) {
            ChessMove m = iterator.next();
            //clone board, move piece, check if is in check
            ChessGame cloneGame = this.clone();

            try {
                cloneGame.makeMove(m);
            } catch (InvalidMoveException e) {
                //if in check, remove that move
                iterator.remove(); // Remove the current element using the iterator
            }
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        ChessPiece piece = gameBoard.getPiece(move.getStartPosition());
        TeamColor teamTurn = getTeamTurn();
        if (teamTurn == null && piece != null) {
            teamTurn = piece.getTeamColor();
        }
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        boolean hasEnd = false;

        if (piece != null) {
            ArrayList<ChessMove> possMoves = new ArrayList<>(piece.pieceMoves(gameBoard, startPos));
            for (ChessMove m : possMoves) {
                if (m.getEndPosition().equals(endPos)) {
                    hasEnd = true;
                }
            }
            //if cannot move there
            if (!hasEnd) {
                throw new InvalidMoveException("The piece cannot move to the desired end position");
            }
            //elif move.startposition.piece.getteam != teamTurn
            else if (piece.getTeamColor() != teamTurn) {
                throw new InvalidMoveException("It is not the appropriate team's turn");
            }
            //else
            else {
                ChessGame cloneGame = this.clone();
                cloneGame.getBoard().movePiece(startPos,endPos,piece);
                //if cloneBoard's 'game' isn't in check, then do the code below. otherwise throw an exception.
                if (cloneGame.isInCheck(piece.getTeamColor())) {
                    throw new InvalidMoveException("This move will leave your King in check");
                }
                else {
                    gameBoard.movePiece(startPos,endPos,piece);
                    if (teamTurn.equals(TeamColor.BLACK)) {
                        setTeamTurn(TeamColor.WHITE);
                    }
                    else if (teamTurn.equals(TeamColor.WHITE)) {
                        setTeamTurn(TeamColor.BLACK);
                    }

                }
            }
        }
        else throw new InvalidMoveException("There is no piece at your selected start position");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        BoardStats eStats = new BoardStats(this);
        ArrayList<ChessMove> oppMoves = new ArrayList<>(eStats.enemyMoves(teamColor));
        ChessPosition kingPos = eStats.findKing(teamColor);
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
        //use friendlyMoves; if empty then true
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

    //TESTED -- THIS WORKS!
    @Override
    public ChessGame clone() {
        ChessGame cloneGame = new ChessGame();
        cloneGame.setBoard(gameBoard.clone());
        return cloneGame;
    }
}

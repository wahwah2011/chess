package ui;

import chess.ChessPosition;
import client.ChessClient;
import net.ServerFacade;
import ui.chessboard.DrawBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class InGameUI {

    public ChessClient client;
    public ServerFacade serverFacade;
    public Scanner scanner;

    public InGameUI(ChessClient client, ServerFacade serverFacade, Scanner scanner) {
        this.client = client;
        this.serverFacade = serverFacade;
        this.scanner = scanner;
    }

    public void run() {
        gameUI();
    }

    private void gameUI() {
        System.out.println("[In game \"" + client.getGameName() + "\"] Commands: Help, Redraw, Make Move, Highlight Moves, Leave, Resign");
        System.out.println("Please enter a command >>> \n");
        String command = scanner.nextLine().trim().toLowerCase();
        DrawBoard board = new DrawBoard();

        switch (command) {
            case "help":
                showGameHelp();
                break;
            case "redraw":
                redrawBoard();
                break;
            case "make move":
                makeMove();
                break;
            case "highlight moves":
                highlightMoves();
                break;
            case "leave":
                leave();
                break;
            case "resign":
                resign();
                break;
            default:
                System.out.println("Invalid command. Type 'Help' to see available commands.");
        }
    }

    private void showGameHelp() {
        System.out.println("Available commands:");
        System.out.println("Help - Displays this help text.");
        System.out.println("Redraw - Redraws current board.");
        System.out.println("Make Move - Make legal piece move on your turn.");
        System.out.println("Highlight Moves - Highlights all available moves for a specified piece.");
        System.out.println("Leave - Leave current game.");
        System.out.println("Resign - Forfeit current game.\n");
    }

    private void redrawBoard() {
        DrawBoard board = new DrawBoard(client.getBoard());
        board.drawChessBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), client.getTeamColor());
    }

    private void makeMove() {

    }

    private void highlightMoves() {
        String position = getValidChessPosition();
        ChessPosition chessPosition = convertToChessPosition(position);
        DrawBoard drawBoard = new DrawBoard(client.getBoard());
        drawBoard.highlightMoveBoard(chessPosition, new PrintStream(System.out, true, StandardCharsets.UTF_8), client.getTeamColor());
    }

    private void leave() {
        // serverFacade.leaveGame(authToken, teamColor, gameID);
        client.setInGame(false);
        client.setObservingGame(false);
        client.setGameID(null);
    }

    private void resign() {
        client.setInGame(false);
    }

    private ChessPosition convertToChessPosition(String position) {
        int row = Character.getNumericValue(position.charAt(0));
        int column = position.charAt(1) - 'a' + 1;

        return new ChessPosition(row, column);
    }

    private String getValidChessPosition() {
        Scanner scanner = new Scanner(System.in);
        String position;

        while (true) {
            System.out.println("Enter a chess position (e.g., 1a, 3d): ");
            position = scanner.nextLine().trim().toLowerCase();

            if (isValidPosition(position)) {
                break;
            } else {
                client.printErrorMessage("Invalid position. Please enter a valid position (rows 1-8, columns a-h).");
            }
        }

        return position;
    }

    private boolean isValidPosition(String position) {
        if (position.length() != 2) {
            return false;
        }

        char row = position.charAt(0);
        char column = position.charAt(1);

        return (row >= '1' && row <= '8') && (column >= 'a' && column <= 'h');
    }
}

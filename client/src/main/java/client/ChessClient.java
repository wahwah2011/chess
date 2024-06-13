package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.*;
import net.ServerFacade;
import ui.InGameUI;
import ui.PostLoginUI;
import ui.PreLoginUI;
import ui.chessboard.DrawBoard;
import websocket.messages.*;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient implements ServerMessageObserver {
    private boolean isLoggedIn = false;
    private boolean isInGame = false;
    private boolean observingGame = false;
    private String authToken = null;
    private String gameName = null;
    private Integer gameID = null;
    private final ServerFacade serverFacade;
    private String teamColor = null;
    private ChessBoard board = null;

    public ChessClient(int port) throws Exception {
        serverFacade = new ServerFacade(port, this);
    }

    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        PreLoginUI preLoginUI = new PreLoginUI(this, serverFacade, scanner);
        PostLoginUI postLoginUI = new PostLoginUI(this, serverFacade, scanner);
        InGameUI inGameUI = new InGameUI(this, serverFacade, scanner);

        while (true) {
            if (!isLoggedIn()) {
                preLoginUI.run();
            }
            else if (!isInGame) {
                postLoginUI.run();
            }
            else {
                inGameUI.run();
            }
        }
    }


    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    public ChessBoard getBoard() {
        return board;
    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public boolean isObservingGame() {
        return observingGame;
    }

    public void setObservingGame(boolean observingGame) {
        this.observingGame = observingGame;
    }

    private void gameUI(Scanner scanner) {
        System.out.println("[In game \"" + this.gameName + "\"] Commands: Help, Redraw, Make Move, Highlight Moves, Leave, Resign");
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
                makeMove(scanner);
                break;
            case "highlight moves":
                highlightMoves(scanner);
                break;
            case "leave":
                leave();
                isInGame = false;
                break;
            case "resign":
                resign(scanner);
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

    public ArrayList<GameData> listGames() {
        GameList gameData = null;
        ArrayList<GameData> games = null;
        try {
            gameData = serverFacade.listGames(this.authToken);
            games = gameData.games();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Current games:");
        displayGames(games);
        return games;
    }

    private void redrawBoard() {
        DrawBoard board = new DrawBoard(this.board);
        board.drawChessBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), this.teamColor);
    }

    private void makeMove(Scanner scanner) {

    }

    private void highlightMoves(Scanner scanner) {
        String position = getValidChessPosition();
        ChessPosition chessPosition = convertToChessPosition(position);
        DrawBoard drawBoard = new DrawBoard();
        drawBoard.highlightMoveBoard(chessPosition, new PrintStream(System.out, true, StandardCharsets.UTF_8), this.teamColor);
    }

    private void leave() {
       // serverFacade.leaveGame(authToken, teamColor, gameID);
        isInGame = false;
        gameID = null;
    }

    private void resign(Scanner scanner) {
        isInGame = false;
    }

    private boolean isLoggedIn() {
        return this.isLoggedIn && this.authToken != null;
    }

    public void setAuth(AuthData auth) {
        this.authToken = auth.authToken();
        if (this.authToken != null) {
            isLoggedIn = true;
            System.out.println("Successfully logged in.\n");
        }
    }

    public void authMessage(AuthData auth) {
        if (auth.message() != null) {
            printErrorMessage(auth.message());
        }
    }

    private void displayGames(ArrayList<GameData> games) {
        if (!games.isEmpty()) {
            for (int i = 0; i < games.size(); i++) {
                GameData game = games.get(i);
                System.out.print(i + ") " + "Name: \"" + game.gameName() + "\"\n");
                if (game.whiteUsername() != null) {
                    System.out.print("White player: " + game.whiteUsername() + "\n");
                }
                else System.out.print("White player: None \n");
                if (game.blackUsername() != null) {
                    System.out.print("Black player: " + game.blackUsername() + "\n");
                }
                else System.out.print("Black player: None \n");
                System.out.print('\n');
            }
        }
        else System.out.println("Currently no games in session. Create a new game if you would like to play.\n");
    }

    public Integer assignGameID(int index, ArrayList<GameData> games) {
        if (index < 0 || index >= games.size()) {
            printErrorMessage("Please enter a valid index!");
            return null;
        } else {
            return games.get(index).gameID();
        }
    }

    public String getValidChessPosition() {
        Scanner scanner = new Scanner(System.in);
        String position;

        while (true) {
            System.out.println("Enter a chess position (e.g., 1a, 3d): ");
            position = scanner.nextLine().trim().toLowerCase();

            if (isValidPosition(position)) {
                break;
            } else {
                printErrorMessage("Invalid position. Please enter a valid position (rows 1-8, columns a-h).");
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

    public ChessPosition convertToChessPosition(String position) {
        int row = Character.getNumericValue(position.charAt(0));
        int column = position.charAt(1) - 'a' + 1;

        return new ChessPosition(row, column);
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> printNotification(((NotificationMessage) message).getMessage());
            case ERROR -> printErrorMessage(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }

    public void loadGame(String game) {
        Gson serializer = new Gson();
        ChessGame chessGame = serializer.fromJson(game, ChessGame.class);
        DrawBoard board = new DrawBoard(chessGame.getBoard());
        if (this.teamColor != null) {
            board.drawChessBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), this.teamColor);
        }
        else board.drawObserverView(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    }

    public void printNotification(String message) {
        System.out.print(SET_TEXT_COLOR_GREEN);
        printMessage(message);
    }

    public void printErrorMessage(String message) {
        System.out.print(SET_TEXT_COLOR_RED);
        printMessage(message);
    }

    private void printMessage(String message) {
        System.out.println("\n" + message);
        System.out.print(SET_TEXT_COLOR_BLACK);
        System.out.print("\n");
    }

}

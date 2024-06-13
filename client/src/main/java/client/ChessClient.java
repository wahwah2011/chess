package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.*;
import net.ServerFacade;
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
    private int port;
    private PreLoginUI preLoginUI;
    private boolean isLoggedIn = false;
    private boolean isInGame = false;
    private String authToken = null;
    private String gameName = null;
    private Integer gameID = null;
    private ServerFacade serverFacade;
    private String teamColor = null;
    private ChessBoard board = null;

    public ChessClient(int port) throws Exception {
        this.port = port;
        serverFacade = new ServerFacade(port, this);
    }

    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        preLoginUI = new PreLoginUI(this, serverFacade, scanner);
        while (true) {
            if (!isLoggedIn()) {
                preLoginUI.run();
            }
            else if (!isInGame) {
                postLoginUI(scanner);
            }
            else {
                gameUI(scanner);
            }
        }
    }

    private void preLoginUI(Scanner scanner) throws IOException {
        System.out.println("[Logged out] Commands: Help, Quit, Login, Register");
        System.out.print("Please enter a command >>> \n");
        String command = scanner.nextLine().trim().toLowerCase();

        switch (command) {
            case "help":
                showPreLoginHelp();
                break;
            case "quit":
                System.out.println("Exiting the program...");
                System.exit(0);
                break;
            case "login":
                login(scanner);
                break;
            case "register":
                register(scanner);
                break;
            default:
                System.out.println("Invalid command. Type 'Help' to see available commands.\n");
        }
    }

    private void postLoginUI(Scanner scanner) {
        System.out.println("[Logged in] Commands: Help, Logout, Create Game, List Games, Play Game, Observe Game");
        System.out.print("Please enter a command >>> \n");
        String command = scanner.nextLine().trim().toLowerCase();

        switch (command) {
            case "help":
                showPostLoginHelp();
                break;
            case "logout":
                logout();
                break;
            case "create game":
                createGame(scanner);
                break;
            case "list games":
                listGames();
                break;
            case "play game":
                playGame(scanner);
                break;
            case "observe game":
                observeGame(scanner);
                break;
            default:
                System.out.println("Invalid command. Type 'Help' to see available commands.");
        }
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

    private void showPreLoginHelp() {
        System.out.println("Available commands:");
        System.out.println("Help - Displays this help text.");
        System.out.println("Quit - Exits the program.");
        System.out.println("Login - Prompts for login information.");
        System.out.println("Register - Prompts for registration information.\n");
    }

    private void showPostLoginHelp() {
        System.out.println("Available commands:");
        System.out.println("Help - Displays this help text.");
        System.out.println("Logout - Logs out the user.");
        System.out.println("Create Game - Creates a new game.");
        System.out.println("List Games - Lists all available games.");
        System.out.println("Play Game - Joins a game as a player.");
        System.out.println("Observe Game - Observes a game.\n");
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

    private void register(Scanner scanner) {
        AuthData auth = null;
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        try {
            auth = serverFacade.registerFacade(username,password,email);
            authMessage(auth);
            setAuth(auth);
        } catch (Exception e) {
            printErrorMessage("Unable to register.");
        }
    }

    private void login(Scanner scanner) {
        AuthData auth = null;
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        try {
            auth = serverFacade.login(username,password);
            authMessage(auth);
            setAuth(auth);
        } catch (IOException e) {
            printErrorMessage("Unable to login.");
        }
    }

    private void logout() {
        try {
            serverFacade.logout(this.authToken);
            System.out.println("Successfully logged out.\n");
            isLoggedIn = false;
            authToken = null;
        } catch (IOException e) {
            printErrorMessage("Unable to log out.");
        }
    }

    private void createGame(Scanner scanner) {
        GameData createdGame = null;
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine().trim();
        try {
            createdGame = serverFacade.createGame(this.authToken, gameName);
            System.out.println("Game \"" + createdGame.gameName() + "\" created successfully.\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<GameData> listGames() {
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

    private void playGame(Scanner scanner) {
        AuthData response = null;
        ChessGame.TeamColor color = null;
        ArrayList<GameData> games = null;
        games = listGames();

        while (this.gameID == null) {
            System.out.print("Enter game number: ");
            try {
                int index = Integer.parseInt(scanner.nextLine().trim());
                this.gameID = assignGameID(index,games);
                if (this.gameID != null) {
                    this.gameName = games.get(index).gameName();
                }
            } catch (Exception e) {
                printErrorMessage("Please enter an existing game number!");
            }
        }
        while(color == null) {
            System.out.print("Enter team color (white/black): ");
            this.teamColor = scanner.nextLine().trim().toLowerCase();
            if (this.teamColor.equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (this.teamColor.equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                printErrorMessage("Please enter a valid color! (white/black)");
                this.teamColor = null;
            }
        }
        try {
            response = serverFacade.joinGame(this.authToken,color,this.gameID);
            if (response.message() == null) {
                isInGame = true;
                System.out.println("Joined game " + this.gameName + " successfully.\n");
            }
            else authMessage(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void observeGame(Scanner scanner) {
        Integer gameNumber = null;
        ArrayList<GameData> games = listGames();

        while (gameNumber == null) {
            System.out.print("Enter game number: ");
            try {
                int index = Integer.parseInt(scanner.nextLine().trim());
                gameNumber = assignGameID(index,games);
                if (gameNumber != null) {
                    this.gameName = games.get(index).gameName();
                }
            } catch (Exception e) {
                printErrorMessage("Please enter an existing game number!");
            }
        }
        serverFacade.observeGame(authToken,gameNumber);
        System.out.println("Observing game " + gameName + ".\n");
    }

    private void redrawBoard() {
        DrawBoard board = new DrawBoard();
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
    }

    private void resign(Scanner scanner) {

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

    private Integer assignGameID(int index, ArrayList<GameData> games) {
        if (index < 0 || index >= games.size()) {
            printErrorMessage("Please enter a valid index!");
            return null;
        } else {
            return games.get(index).gameID();
        }
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

package ui;

import chess.ChessGame;
import model.*;
import net.ServerFacade;
import ui.chessboard.DrawBoard;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChessClient {
    private int port;
    private boolean isLoggedIn = false;
    private String authToken = null;
    private ServerFacade serverFacade;
    //private String userTeam; for keeping track of team in printing board, making moves?

    public ChessClient(int port) {
        this.port = port;
        serverFacade = new ServerFacade(port);
    }
    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!isLoggedIn()) {
                preLoginUI(scanner);
            } else {
                postLoginUI(scanner);
            }
        }
    }

    private void preLoginUI(Scanner scanner) throws IOException {
        System.out.println("[Logged out] Commands: Help, Quit, Login, Register");
        System.out.print("Please enter a command >>> ");
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
                System.out.println("Invalid command. Type 'Help' to see available commands.");
        }
    }

    private void postLoginUI(Scanner scanner) {
        System.out.println("[Logged in] Commands: Help, Logout, Create Game, List Games, Play Game, Observe Game");
        System.out.print("Please enter a command >>> ");
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

    private void showPreLoginHelp() {
        System.out.println("Available commands:");
        System.out.println("Help - Displays this help text.");
        System.out.println("Quit - Exits the program.");
        System.out.println("Login - Prompts for login information.");
        System.out.println("Register - Prompts for registration information.");
    }

    private void showPostLoginHelp() {
        System.out.println("Available commands:");
        System.out.println("Help - Displays this help text.");
        System.out.println("Logout - Logs out the user.");
        System.out.println("Create Game - Creates a new game.");
        System.out.println("List Games - Lists all available games.");
        System.out.println("Play Game - Joins a game as a player.");
        System.out.println("Observe Game - Observes a game.");
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
            System.out.println(e + "; Unable to register.");
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
            System.out.println(e + "; Unable to login.");
        }
    }

    private void logout() {
        try {
            serverFacade.logout(this.authToken);
            System.out.println("Successfully logged out.\n");
            isLoggedIn = false;
            authToken = null;
        } catch (IOException e) {
            System.out.println("Unable to log out.");
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

    private void listGames() {
        GameList games = null;
        try {
            games = serverFacade.listGames(this.authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Current games:");
        displayGames(games);
    }

    private void playGame(Scanner scanner) {
        AuthData response = null;
        String teamColor = null;
        ChessGame.TeamColor color = null;
        Integer gameNumber = null;
        listGames();
        while (gameNumber == null) {
            System.out.print("Enter game number: ");
            try {
                gameNumber = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Please enter a string value!\n");
            }
        }
        while(color == null) {
            System.out.print("Enter team color (white/black): ");
            teamColor = scanner.nextLine().trim().toLowerCase();
            if (teamColor.equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (teamColor.equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else System.out.println("Please enter a valid color!\n");
        }
        try {
            response = serverFacade.joinGame(this.authToken,color,gameNumber);
            if (response.message() == null) {
                System.out.println("Joined game successfully.");
                DrawBoard board = new DrawBoard();
                board.drawChessBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), teamColor);
            }
            else authMessage(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void observeGame(Scanner scanner) {
        Integer gameNumber = null;
        listGames();
        while(gameNumber == null) {
            System.out.print("Enter game number: ");
            try {
                gameNumber = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Please enter a string value!");
            }
        }
        System.out.println("Observing game " + gameNumber);
        DrawBoard board = new DrawBoard();
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        board.drawObserverView(out);
    }

    private boolean isLoggedIn() {
        return this.isLoggedIn && this.authToken != null;
    }

    private void setAuth(AuthData auth) {
        this.authToken = auth.authToken();
        if (this.authToken != null) {
            isLoggedIn = true;
            System.out.println("Successfully logged in.\n");
        }
    }

    private void authMessage(AuthData auth) {
        if (auth.message() != null) {
            System.out.println(auth.message() + "\n");
        }
    }

    //come back to this; display games available to play one by one
    public void displayGames(GameList gameList) {
        if (!gameList.games().isEmpty()) {
            int counter = 1;
            for (GameData game : gameList.games()) {
                System.out.println(counter + ") GameID = " + game.gameID() + ", Name: \"" + game.gameName() + "\"");
                counter++;
            }
            System.out.println();
        }
        else System.out.println("Currently no games in session. Create a new game if you would like to play.\n");
    }
}

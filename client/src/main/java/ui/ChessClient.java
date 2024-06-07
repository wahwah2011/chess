package ui;

import model.AuthData;
import model.GameData;
import net.ServerFacade;
import ui.chessboardDisplay.DrawBoard;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {
    private boolean isLoggedIn = false;
    private String authToken = null;
    private final ServerFacade serverFacade = new ServerFacade();
    //private String userTeam; for keeping track of team in printing board, making moves?

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
            System.out.println("Successfully logged out.");
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
            System.out.println("Game \"" + createdGame.gameName() + "\" created successfully.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void listGames() {

    }

    private void playGame(Scanner scanner) {
        Integer gameNumber = null;
        while(gameNumber == null) {
            System.out.print("Enter game number: ");
            try {
                gameNumber = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Please enter a string value!\n");
            }
        }
        System.out.print("Enter team color (white/black): ");
        String teamColor = scanner.nextLine().trim().toLowerCase();
        System.out.println("Joined game successfully.");
        DrawBoard board = new DrawBoard();
        board.drawChessBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), teamColor);
    }

    private void observeGame(Scanner scanner) {
        Integer gameNumber = null;
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
            System.out.println("Successfully logged in");
        }
    }

    private void authMessage(AuthData auth) {
        if (auth.message() != null) {
            System.out.println(auth.message());
        }
    }
}

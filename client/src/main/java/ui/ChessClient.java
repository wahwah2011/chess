package ui;

import java.util.Scanner;

public class ChessClient {
    private boolean isLoggedIn = false;
    private String authToken = null;
    //private String userTeam; for keeping track of team in printing board, making moves?

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (!isLoggedIn) {
                preLoginUI(scanner);
            } else {
                postLoginUI(scanner);
            }
        }
    }

    private void preLoginUI(Scanner scanner) {
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

    private void login(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        //
        isLoggedIn = true;
        this.authToken = "placeHolderAuth";
        System.out.println("Successfully logged in as " + username);
    }

    private void register(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        isLoggedIn = true;
        this.authToken = "placeHolderAuth";
        System.out.println("Successfully registered and logged in as " + username);
    }

    private void logout() {
        isLoggedIn = false;
        authToken = null;
        System.out.println("Successfully logged out.");
    }

    private void createGame(Scanner scanner) {
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine().trim();
        System.out.println("Game created successfully.");
    }

    private void listGames() {

    }

    private void playGame(Scanner scanner) {
        System.out.print("Enter game number: ");
        int gameNumber = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter team color (white/black): ");
        String teamColor = scanner.nextLine().trim().toLowerCase();
        System.out.println("Joined game successfully.");
    }

    private void observeGame(Scanner scanner) {
        System.out.print("Enter game number: ");
        int gameNumber = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Observing game " + gameNumber);
    }
}

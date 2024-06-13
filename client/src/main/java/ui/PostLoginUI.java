package ui;

import chess.ChessGame;
import client.ChessClient;
import model.AuthData;
import model.GameData;
import net.ServerFacade;
import ui.chessboard.DrawBoard;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class PostLoginUI {

    public ChessClient client;
    public ServerFacade serverFacade;
    public Scanner scanner;

    public PostLoginUI(ChessClient client, ServerFacade serverFacade, Scanner scanner) {
        this.client = client;
        this.serverFacade = serverFacade;
        this.scanner = scanner;
    }

    public void run() {
        postLoginUI();
    }

    private void postLoginUI() {
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
                client.listGames();
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

    private void showPostLoginHelp() {
        System.out.println("Available commands:");
        System.out.println("Help - Displays this help text.");
        System.out.println("Logout - Logs out the user.");
        System.out.println("Create Game - Creates a new game.");
        System.out.println("List Games - Lists all available games.");
        System.out.println("Play Game - Joins a game as a player.");
        System.out.println("Observe Game - Observes a game.\n");
    }

    private void logout() {
        try {
            serverFacade.logout(client.getAuthToken());
            System.out.println("Successfully logged out.\n");
            client.setLoggedIn(false);
            client.setAuthToken(null);
        } catch (IOException e) {
            client.printErrorMessage("Unable to log out.");
        }
    }

    private void createGame(Scanner scanner) {
        GameData createdGame = null;
        System.out.print("Enter game name: ");
        String gameName = scanner.nextLine().trim();
        try {
            createdGame = serverFacade.createGame(client.getAuthToken(), gameName);
            System.out.println("Game \"" + createdGame.gameName() + "\" created successfully.\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void playGame(Scanner scanner) {
        AuthData response = null;
        ChessGame.TeamColor color = null;
        ArrayList<GameData> games = null;
        games = client.listGames();
        int index = 0;

        while (client.getGameID() == null) {
            System.out.print("Enter game number: ");
            try {
                index = Integer.parseInt(scanner.nextLine().trim());
                client.setGameID(client.assignGameID(index,games));
                if (client.getGameID() != null) {
                }
            } catch (Exception e) {
                client.printErrorMessage("Please enter an existing game number!");
            }
        }
        while(color == null) {
            System.out.print("Enter team color (white/black): ");
            client.setTeamColor(scanner.nextLine().trim().toLowerCase());
            if (client.getTeamColor().equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (client.getTeamColor().equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                client.printErrorMessage("Please enter a valid color! (white/black)");
                client.setTeamColor(null);
            }
        }
        try {
            response = serverFacade.joinGame(client.getAuthToken(),color,client.getGameID());
            if (response.message() == null) {
                client.setInGame(true);
                System.out.println("Joined game " + client.getGameName() + " successfully.\n");
                client.setGameName(games.get(index).gameName());
            }
            else {
                client.authMessage(response);
                client.setGameID(null);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void observeGame(Scanner scanner) {
        Integer gameNumber = null;
        ArrayList<GameData> games = client.listGames();

        while (gameNumber == null) {
            System.out.print("Enter game number: ");
            try {
                int index = Integer.parseInt(scanner.nextLine().trim());
                gameNumber = client.assignGameID(index,games);
                if (gameNumber != null) {
                    client.setGameName(games.get(index).gameName());
                }
            } catch (Exception e) {
                client.printErrorMessage("Please enter an existing game number!");
            }
        }
        serverFacade.observeGame(client.getAuthToken(),gameNumber);
        System.out.println("Observing game " + client.getGameName() + ".\n");
        client.setObservingGame(true);
    }
}

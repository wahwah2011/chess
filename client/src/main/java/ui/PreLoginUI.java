package ui;

import client.ChessClient;
import model.AuthData;
import net.ServerFacade;

import java.io.IOException;
import java.util.Scanner;

public class PreLoginUI {
    public ChessClient client;
    public ServerFacade serverFacade;
    public Scanner scanner;

    public PreLoginUI(ChessClient client, ServerFacade serverFacade, Scanner scanner) {
        this.client = client;
        this.serverFacade = serverFacade;
        this.scanner = scanner;
    }

    public void run() {
        preLoginUI();
    }


    private void preLoginUI() {
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
                login();
                break;
            case "register":
                register();
                break;
            default:
                System.out.println("Invalid command. Type 'Help' to see available commands.\n");
        }
    }

    private void showPreLoginHelp() {
        System.out.println("Available commands:");
        System.out.println("Help - Displays this help text.");
        System.out.println("Quit - Exits the program.");
        System.out.println("Login - Prompts for login information.");
        System.out.println("Register - Prompts for registration information.\n");
    }

    private void register() {
        AuthData auth = null;
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();
        try {
            auth = serverFacade.registerFacade(username,password,email);
            client.authMessage(auth);
            setAuth(auth);
        } catch (Exception e) {
            client.printErrorMessage("Unable to register.");
        }
    }

    private void login() {
        AuthData auth = null;
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        try {
            auth = serverFacade.login(username,password);
            client.authMessage(auth);
            setAuth(auth);
        } catch (IOException e) {
            client.printErrorMessage("Unable to login.");
        }
    }

    public void setAuth(AuthData auth) {
        client.setAuthToken(auth.authToken());
        if (client.getAuthToken() != null) {
            client.setLoggedIn(true);
            System.out.println("Successfully logged in.\n");
        }
    }

}

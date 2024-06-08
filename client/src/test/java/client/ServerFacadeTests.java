package client;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade = new ServerFacade();
    static String USERNAME = "test_user";
    static String PASSWORD = "test_password";
    static String EMAIL = "test@example.com";
    static String GAME_NAME = "test_game";

    @BeforeAll
    public static void init() throws IOException {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setUp() throws IOException {
        serverFacade.clear();
    }


    @Test
    void register() {
        try {
            AuthData response = serverFacade.registerFacade(USERNAME, PASSWORD, EMAIL);
            assertFalse(response.authToken().isEmpty());
        } catch (Exception e) {
            fail("Registration failed: " + e.getMessage());
        }
    }

    @Test
    void registerFail() {
        try {
            serverFacade.registerFacade(USERNAME, PASSWORD, EMAIL);
            AuthData response = serverFacade.registerFacade(USERNAME, PASSWORD, EMAIL);
            assertFalse(response.message().isEmpty());
        } catch (Exception e) {
            fail("Registration failed: " + e.getMessage());
        }
    }

    @Test
    void login() {
        try {
            // Login should succeed if the user is registered
            serverFacade.registerFacade(USERNAME, PASSWORD, EMAIL);
            assertNotNull(serverFacade.login(USERNAME, PASSWORD));
        } catch (Exception e) {
            fail("Login failed: " + e.getMessage());
        }
    }

    @Test
    void loginFail() {
        try {
            // response should contain message if unregistered
            AuthData response =serverFacade.login(USERNAME, PASSWORD);
            assertFalse(response.message().isEmpty());
        } catch (Exception e) {
            fail("Login failed: " + e.getMessage());
        }
    }

    @Test
    void logout() {
        try {
            serverFacade.registerFacade(USERNAME, PASSWORD, EMAIL);
            AuthData loginResponse = serverFacade.login(USERNAME, PASSWORD);
            // Logout should succeed if the user is logged in
            assertNull(serverFacade.logout(loginResponse.authToken()).message());
        } catch (Exception e) {
            fail("Logout failed: " + e.getMessage());
        }
    }

    @Test
    void logoutFail() {
        try {
            assertNotNull(serverFacade.logout("fakeAuthToken").message());
            // Assert that no exceptions were thrown
        } catch (Exception e) {
            fail("Logout failed: " + e.getMessage());
        }
    }

    @Test
    void createGame() {
        try {
            String auth = registerLogin();
            assertNotNull(serverFacade.createGame(auth, GAME_NAME).gameID());
        } catch (IOException e) {
            fail("Create game failed: " + e.getMessage());
        }
    }

    @Test
    void createGameFail() {
        try {
            serverFacade.registerFacade(USERNAME, PASSWORD, EMAIL);
            assertNotNull(serverFacade.createGame("fakeAuthToken", GAME_NAME).message());
        } catch (IOException e) {
            fail("Create game failed: " + e.getMessage());
        }
    }

    @Test
    void listGames() {
        try {
            String auth = registerLogin();
            serverFacade.createGame(auth, GAME_NAME + "_1");
            serverFacade.createGame(auth, GAME_NAME + "_2");
            serverFacade.createGame(auth, GAME_NAME + "_3");
            assertNotNull(serverFacade.listGames(auth));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listGamesFail() {
        try {
            String auth = registerLogin();
            serverFacade.createGame(auth, GAME_NAME + "_1");
            serverFacade.createGame(auth, GAME_NAME + "_2");
            serverFacade.createGame(auth, GAME_NAME + "_3");
            assertNotNull(serverFacade.listGames("fakeAuthToken").message());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void joinGame() {
        try {
            String auth = registerLogin();
            Integer gameID = serverFacade.createGame(auth, GAME_NAME).gameID();
            assertNull(serverFacade.joinGame(auth, ChessGame.TeamColor.BLACK,gameID).message());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void joinGameFail() {
        try {
            String auth = registerLogin();
            Integer gameID = serverFacade.createGame(auth, GAME_NAME).gameID();
            assertNotNull(serverFacade.joinGame("fakeAuthToken", ChessGame.TeamColor.BLACK,gameID).message());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void clear() {
        try {
            serverFacade.registerFacade(USERNAME, PASSWORD, EMAIL);
            serverFacade.clear();
            //should be clear and therefore not able to log in, thus response will contain message
            assertNotNull(serverFacade.login(USERNAME,PASSWORD).message());
        } catch (Exception e) {
            fail("Clear failed: " + e.getMessage());
        }
    }

    private String registerLogin() throws IOException {
        serverFacade.registerFacade(USERNAME, PASSWORD, EMAIL);
        AuthData loginResponse = serverFacade.login(USERNAME, PASSWORD);
        return loginResponse.authToken();
    }
    // Similarly, write tests for other methods like listGames, createGame, joinGame, clear, etc.
    // Ensure to handle exceptions and assert the expected behavior.
}

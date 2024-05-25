package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.GameList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    GameService gameService;
    MemoryAuthDAO authDAO;
    MemoryGameDAO gameDAO;
    AuthDAO expectedAuthDAO;
    GameDAO expectedGameDAO;

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        expectedAuthDAO = new MemoryAuthDAO();
        expectedGameDAO = new MemoryGameDAO();
        gameService = new GameService(authDAO,gameDAO);
    }

    @Test
    void createGamePass() throws DataAccessException {
        AuthData auth = new AuthData("12345", "gameUser",null);

        authDAO.createAuth(auth);

        GameData newGame = new GameData(null, null, null, "myGame", null, null);
        GameData response;
        response = gameService.createGame(auth, newGame);

        System.out.println(response.toString());
        String errMessage = "Error: unauthorized";

        assertTrue(response.message() != errMessage);
    }

    @Test
    void createGameFail() throws DataAccessException {
        AuthData auth = new AuthData("12345", "gameUser",null);

        GameData newGame = new GameData(null, null, null, "myGame", null, null);
        GameData response;
        response = gameService.createGame(auth, newGame);

        System.out.println(response.toString());
        String errMessage = "Error: unauthorized";

        assertTrue(response.message() == errMessage);
    }

    @Test
    void listGamesPass() throws DataAccessException {
        AuthData auth = new AuthData("12345", "gameUser",null);

        authDAO.createAuth(auth);

        GameData firstGame = new GameData(null, null, null, "game1", null, null);
        GameData secondGame = new GameData(null, null, null, "game2", null, null);
        GameData thirdGame = new GameData(null, null, null, "game3", null, null);

        gameService.createGame(auth,firstGame);
        gameService.createGame(auth,secondGame);
        gameService.createGame(auth,thirdGame);

        GameList list = gameService.listGames(auth);

        System.out.println(list.toString());
        assertTrue(list.message() == null);
    }

    @Test
    void listGamesFail() throws DataAccessException {
        AuthData auth = new AuthData("12345", "gameUser",null);
        AuthData falseAuth = new AuthData(null,null,null);

        authDAO.createAuth(auth);

        GameData firstGame = new GameData(null, null, null, "game1", null, null);
        GameData secondGame = new GameData(null, null, null, "game2", null, null);
        GameData thirdGame = new GameData(null, null, null, "game3", null, null);

        gameService.createGame(auth,firstGame);
        gameService.createGame(auth,secondGame);
        gameService.createGame(auth,thirdGame);

        GameList list = gameService.listGames(falseAuth);

        System.out.println(list.toString());

        String expectedMessage = "Error: unauthorized";
        assertTrue(list.message() == expectedMessage);
    }

    @Test
    void joinGamePass() {
    }

    @Test
    void joinGameFail() {
    }
}
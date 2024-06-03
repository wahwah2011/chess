package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.GameList;
import model.JoinRequest;
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

        assertNotSame(response.message(), errMessage);
    }

    @Test
    void createGameFail() throws DataAccessException {
        AuthData auth = new AuthData("12345", "gameUser",null);

        GameData newGame = new GameData(null, null, null, "myGame", null, null);
        GameData response;
        response = gameService.createGame(auth, newGame);

        System.out.println(response.toString());
        String errMessage = "Error: unauthorized";

        assertSame(response.message(), errMessage);
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
        assertNull(list.message());
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
        assertSame(list.message(), expectedMessage);
    }

    @Test
    void joinGamePass() throws DataAccessException {
        GameData testGame = new GameData(1234, null,null,"testGame", null, null);
        gameDAO.addGame(testGame);
        authDAO.createAuth(new AuthData("authToken", "testUser", null));

        AuthData expected = new AuthData(null,null,null);
        JoinRequest data = new JoinRequest(ChessGame.TeamColor.BLACK, 1234);

        AuthData result = gameService.joinGame(new AuthData("authToken",null,null),data);

        System.out.println(result.toString());

        assertEquals(result,expected);
    }

    @Test
    void joinGameFail() throws DataAccessException {
        GameData testGame = new GameData(4321, null,"Your mom!","testGame", null, null);
        gameDAO.addGame(testGame);
        authDAO.createAuth(new AuthData("authToken", "testUser", null));

        AuthData expected = new AuthData(null,null,null);
        JoinRequest data = new JoinRequest(ChessGame.TeamColor.BLACK, 4321);

        AuthData result = gameService.joinGame(new AuthData("authToken",null,null),data);

        System.out.println(result.toString());

        assertNotEquals(result,expected);
    }
}
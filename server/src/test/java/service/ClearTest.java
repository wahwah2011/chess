package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearTest {

    Clear clear;

    AuthDAO authDAO;
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO expectedAuth;
    UserDAO expectedUser;
    GameDAO expectedGame;

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        expectedAuth = new MemoryAuthDAO();
        expectedUser = new MemoryUserDAO();
        expectedGame = new MemoryGameDAO();
        clear = new Clear(authDAO,userDAO,gameDAO);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testClearGame() {
        try {
            authDAO.createAuth(new AuthData("hello","myMom"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            userDAO.createUser(new UserData("mama","password", "email.com"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            gameDAO.createGame(new GameData(12345,"","","practiceGame", new ChessGame()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        clear.clearGame();
        assertEquals(authDAO,expectedAuth);
        assertEquals(userDAO,expectedUser);
        assertEquals(gameDAO,expectedGame);
    }
}
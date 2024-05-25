package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.Response.ClearResponse;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearTest {

    Clear clear;

    MemoryAuthDAO authDAO;
    MemoryUserDAO userDAO;
    MemoryGameDAO gameDAO;
    MemoryAuthDAO expectedAuth;
    MemoryUserDAO expectedUser;
    MemoryGameDAO expectedGame;

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
            authDAO.createAuth(new AuthData("hello","myMom", null));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            userDAO.createUser(new UserData("mama","password", "email.com"));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            gameDAO.createGame(new GameData(12345,"","","practiceGame", new ChessGame(), null));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        ClearResponse clearData = clear.clearGame();
        System.out.println(clearData.toString());

        assertEquals(authDAO,expectedAuth);
        assertEquals(userDAO,expectedUser);
        assertEquals(gameDAO,expectedGame);
    }
}
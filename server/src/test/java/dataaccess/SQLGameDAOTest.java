package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameList;
import model.JoinRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoff.model.TestListResult;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLGameDAOTest extends DAOTest{

    GameData createdGame;
    GameData existingGame = new GameData(null, null,null,"ExistingGame", null, null);

    @BeforeEach
    void setUp() throws DataAccessException {
         fullClear();
         createdGame = gameData.createGame(existingGame);
    }

    @Test
    void createGame() {
        int initialRowCount = getDatabaseRows();
        assertEquals(1, initialRowCount);
    }

    @Test
    void createGameFail() {
        //think of a better test for this
        int initialRowCount = getDatabaseRows();
        assertEquals(1, initialRowCount);
    }

    @Test
    void getGame() throws DataAccessException {
        GameData game = gameData.getGame(new JoinRequest(null, createdGame.gameID()));
        assertEquals(game.gameName(),existingGame.gameName());
    }

    @Test
    void getGameFail() {
        assertThrows(DataAccessException.class, () -> gameData.getGame(new JoinRequest(null,12345)));
    }

    @Test
    void listGames() throws DataAccessException {
        GameList listResult = gameData.listGames();
        assertEquals(1, listResult.games().size());
    }

    @Test
    void listGamesFail() throws DataAccessException {
        fullClear();
        GameList listResult = gameData.listGames();
        assertEquals(0,listResult.games().size());
    }

    @Test
    void updateGame() throws DataAccessException {
        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.BLACK,createdGame.gameID());
        gameData.updateGame(createdGame,joinRequest,"testBlackUser");
        GameData game = gameData.getGame(joinRequest);
        assertEquals(game.blackUsername(), "testBlackUser");
    }

    @Test
    void updateGameFail() throws DataAccessException {
        JoinRequest joinRequest = new JoinRequest(ChessGame.TeamColor.BLACK,createdGame.gameID());
        gameData.updateGame(createdGame,joinRequest,"testBlackUser");
        GameData game = gameData.getGame(joinRequest);
        assertThrows(DataAccessException.class, () -> gameData.updateGame(game,joinRequest,"fakeBlackUser"));
    }

    @Test
    void clear() throws DataAccessException {
        gameData.clear();
        int numRows = getDatabaseRows();
        assertEquals(0,numRows);
    }
}
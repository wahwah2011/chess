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

class SQLGameDAOTest {

    SQLAuthDAO authData = new SQLAuthDAO();
    SQLUserDAO userData = new SQLUserDAO();
    SQLGameDAO gameData = new SQLGameDAO();
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

    private Connection getConnection() throws ReflectiveOperationException {
        Class<?> clazz = Class.forName("dataaccess.DatabaseManager");
        Method getConnectionMethod = clazz.getDeclaredMethod("getConnection");
        getConnectionMethod.setAccessible(true);

        Object obj = clazz.getDeclaredConstructor().newInstance();
        return (Connection) getConnectionMethod.invoke(obj);
    }

    private int getDatabaseRows() {
        int rows = 0;
        try (Connection conn = getConnection();) {
            try (var statement = conn.createStatement()) {
                for (String table : getTables(conn)) {
                    var sql = "SELECT count(*) FROM " + table;
                    try (var resultSet = statement.executeQuery(sql)) {
                        if (resultSet.next()) {
                            rows += resultSet.getInt(1);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Assertions.fail("Unable to load database in order to verify persistence. Are you using dataAccess.DatabaseManager to set your credentials?", ex);
        }

        return rows;
    }

    private List<String> getTables(Connection conn) throws SQLException {
        String sql = """
                    SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema = DATABASE();
                """;

        List<String> tableNames = new ArrayList<>();
        try (var preparedStatement = conn.prepareStatement(sql)) {
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tableNames.add(resultSet.getString(1));
                }
            }
        }

        return tableNames;
    }

    private void fullClear() throws DataAccessException {
        authData.clear();
        userData.clear();
        gameData.clear();
    }

}
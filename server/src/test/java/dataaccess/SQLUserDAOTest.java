package dataaccess;

import model.UserData;
import dataaccess.DatabaseManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {

    SQLAuthDAO authData = new SQLAuthDAO();
    SQLUserDAO userData = new SQLUserDAO();
    SQLGameDAO gameData = new SQLGameDAO();
    UserData existingUser = new UserData("ExistingUser","ExistingPassword", "existingmail@mail.com");

    @BeforeEach
    void setUp() throws DataAccessException {
        fullClear();
        userData.createUser(existingUser);
    }

    @Test
    void createUser() {
        int initialRowCount = getDatabaseRows();
        assertTrue(initialRowCount == 1, "There are a viable number of rows in your database");
    }

    @Test
    void createUserFail() throws DataAccessException {
        UserData copyUser = new UserData("ExistingUser", "fakePassword", "fakeemail@mail.com");

        assertThrows(DataAccessException.class, () -> userData.createUser(copyUser));
    }

    @Test
    void getUser() throws DataAccessException {
        String dbName = userData.getUser(existingUser).username();
        assertEquals(dbName, existingUser.username());
    }

    @Test
    void getUserFail() throws DataAccessException {
        fullClear();
        assertThrows(DataAccessException.class, () -> userData.getUser(existingUser));
    }

    @Test
    void clear() throws DataAccessException {
        fullClear();
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
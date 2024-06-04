package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    SQLAuthDAO authData = new SQLAuthDAO();
    SQLUserDAO userData = new SQLUserDAO();
    SQLGameDAO gameData = new SQLGameDAO();
    UserData existingUser = new UserData("ExistingUser","ExistingPassword", "existingmail@mail.com");
    AuthData existingAuth = new AuthData("authToken", "ExistingUser", null);



    @BeforeEach
    void setUp() throws DataAccessException {
        fullClear();
        userData.createUser(existingUser);
        authData.createAuth(existingAuth);
    }

    @Test
    void createAuth() {
        int initialRowCount = getDatabaseRows();
        assertTrue(initialRowCount == 2, "There are a viable number of rows in your database");
    }

    @Test
    void createAuthFail() throws DataAccessException {
        fullClear();
        assertThrows(DataAccessException.class, () -> authData.createAuth(existingAuth), "Username does not exist");
    }

    @Test
    void getAuth() throws DataAccessException {
        assertEquals(authData.getAuth(existingAuth).authToken(), existingAuth.authToken());
    }

    @Test
    void getAuthFail() {
        AuthData falseData = new AuthData("fakeToken", "ExistingUser", null);
        assertThrows(DataAccessException.class, () -> authData.getAuth(falseData));
    }

    @Test
    void deleteAuth() throws DataAccessException {
        authData.deleteAuth(existingAuth);
        int postCount = getDatabaseRows();
        // should be one because originally 2, but row in authData was deleted
        assertEquals(1, postCount);
    }

    @Test
    void deleteAuthFail() throws DataAccessException {
        AuthData falseData = new AuthData("fakeToken", "ExistingUser", null);
        authData.deleteAuth(falseData);
        int postCount = getDatabaseRows();
        assertEquals(2,postCount);
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
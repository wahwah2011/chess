package dataaccess;

import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOTest {

    SQLAuthDAO authData = new SQLAuthDAO();
    SQLUserDAO userData = new SQLUserDAO();
    SQLGameDAO gameData = new SQLGameDAO();

    Connection getConnection() throws ReflectiveOperationException {
        Class<?> clazz = Class.forName("dataaccess.DatabaseManager");
        Method getConnectionMethod = clazz.getDeclaredMethod("getConnection");
        getConnectionMethod.setAccessible(true);

        Object obj = clazz.getDeclaredConstructor().newInstance();
        return (Connection) getConnectionMethod.invoke(obj);
    }

    int getDatabaseRows() {
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

    List<String> getTables(Connection conn) throws SQLException {
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

    void fullClear() throws DataAccessException {
        authData.clear();
        userData.clear();
        gameData.clear();
    }
}

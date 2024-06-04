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

    Connection startConnection() {
        try {
            Class<?> classy = Class.forName("dataaccess.DatabaseManager");
            Method getConnectionMethod = classy.getDeclaredMethod("getConnection");
            getConnectionMethod.setAccessible(true);

            Object object = classy.getDeclaredConstructor().newInstance();
            return (Connection) getConnectionMethod.invoke(object);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Error accessing database connection", e);
        }
    }

    int dataRows() {
        int numRows = 0;
        try (Connection conn = startConnection()) {
            var statement = conn.createStatement();
            for (String t : listTabs(conn)) {
                String sql = "SELECT count(*) FROM " + t;
                try (var endData = statement.executeQuery(sql)) {
                    if (endData.next()) {
                        numRows += endData.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            Assertions.fail(e);
        }
        return numRows;
    }

    List<String> listTabs(Connection conn) {
        String statement = """
                    SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema = DATABASE();
                """;

        List<String> tables = new ArrayList<>();
        try (var preparedStatement = conn.prepareStatement(statement);
             var endData = preparedStatement.executeQuery()) {
            while (endData.next()) {
                tables.add(endData.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    void fullClear() {
        try {
            authData.clear();
            userData.clear();
            gameData.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException("Error clearing data", e);
        }
    }
}

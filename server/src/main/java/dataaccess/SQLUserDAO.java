package dataaccess;

import model.UserData;

import java.sql.*;

import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() {
        try {
            configureDatabase();
        } catch (Exception e) {
            System.out.println("SQLUserDAO threw an error while initializing");
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        //add user to chess.user according to parameters outlined in UserData
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException {
        String username = user.username();
        UserData returnUser;
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password, email FROM user WHERE username=?";
            try (PreparedStatement stmt = conn.prepareStatement(statement)) {
                stmt.setString(1,username);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String dbUsername = rs.getString(1);
                        String password = rs.getString(2);
                        String email = rs.getString(3);
                        returnUser = new UserData(dbUsername, password, email);
                        return returnUser;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to get user: %s", e.getMessage()));
        }
        throw new DataAccessException("username doesn't exist");
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM user";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof UserDAO p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
                username VARCHAR(255) PRIMARY KEY,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL
            );
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}

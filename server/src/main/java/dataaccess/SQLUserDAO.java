package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        String hashedPassword = hashPassword(userData.password());
        var statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), hashedPassword, userData.email());
    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException {
        String username = user.username();
        String password = hashPassword(user.password());
        UserData returnUser;
        if (!verifyUser(username,user.password())) {
            throw new DataAccessException("incorrect password");
        }
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT username, password FROM user WHERE username=?";
            try (PreparedStatement stmt = conn.prepareStatement(statement)) {
                stmt.setString(1,username);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String dbUsername = rs.getString(1);
                        String dbPassword = rs.getString(2);
                        returnUser = new UserData(dbUsername, dbPassword, null);
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

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean verifyUser(String username, String providedClearTextPassword) throws DataAccessException {
        // read the previously hashed password from the database
        var hashedPassword = readHashedPasswordFromDatabase(username);

        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    private String readHashedPasswordFromDatabase(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "SELECT password FROM user WHERE username=?;";
            try (PreparedStatement stmt = conn.prepareStatement(statement)) {
                stmt.setString(1,username);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        return rs.getString(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to get user: %s", e.getMessage()));
        }
        throw new DataAccessException("unable to access password");
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

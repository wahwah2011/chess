package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.List;

public class SQLUserDAO extends SQLBaseDAO implements UserDAO {

    public SQLUserDAO() {
        try {
            configureDatabase(List.of(
                    """
                    CREATE TABLE IF NOT EXISTS user (
                        username VARCHAR(255) PRIMARY KEY,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL
                    );
                    """
            ));
        } catch (Exception e) {
            System.err.println("SQLUserDAO threw an error while initializing: " + e.getMessage());
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String hashedPassword = hashPassword(userData.password());
        String statement = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, userData.username(), hashedPassword, userData.email());
    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException {
        if (!verifyUser(user.username(), user.password())) {
            throw new DataAccessException("Incorrect password");
        }

        String statement = "SELECT username, password FROM user WHERE username=?";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(statement)) {

            stmt.setString(1, user.username());

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String dbUsername = rs.getString("username");
                    String dbPassword = rs.getString("password");
                    return new UserData(dbUsername, dbPassword, null);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to get user");
        }
        throw new DataAccessException("Username doesn't exist");
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM user";
        executeUpdate(statement);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean verifyUser(String username, String providedClearTextPassword) throws DataAccessException {
        String hashedPassword = readHashedPasswordFromDatabase(username);
        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
    }

    private String readHashedPasswordFromDatabase(String username) throws DataAccessException {
        String statement = "SELECT password FROM user WHERE username=?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(statement)) {

            stmt.setString(1, username);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to get user");
        }
        throw new DataAccessException("Unable to access password");
    }
}

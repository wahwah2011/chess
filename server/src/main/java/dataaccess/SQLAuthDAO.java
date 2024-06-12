package dataaccess;

import model.AuthData;

import java.sql.*;
import java.util.List;

public class SQLAuthDAO extends SQLBaseDAO implements AuthDAO {

    public SQLAuthDAO() {
        try {
            configureDatabase(List.of(
                    """
                    CREATE TABLE IF NOT EXISTS auth (
                        authToken VARCHAR(255) PRIMARY KEY,
                        username VARCHAR(255),
                        FOREIGN KEY (username) REFERENCES user(username)
                    );
                    """
            ));
        } catch (Exception e) {
            System.err.println("SQLAuthDAO threw an error while initializing: " + e.getMessage());
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        String statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        String authToken = authData.authToken();
        String statement = "SELECT authToken, username FROM auth WHERE authToken=?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(statement)) {

            stmt.setString(1, authToken);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String dbAuthToken = rs.getString("authToken");
                    String dbUsername = rs.getString("username");
                    return new AuthData(dbAuthToken, dbUsername, null);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to get auth");
        }
        throw new DataAccessException("Auth token doesn't exist");
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        String statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authData.authToken());
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM auth";
        executeUpdate(statement);
    }

    public String getUsername(String authToken) throws DataAccessException {
        String statement = "SELECT username FROM auth WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(statement)) {

            stmt.setString(1, authToken);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("username");
                    return username;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to get username");
        }
        throw new DataAccessException("Auth token doesn't exist");
    }
}

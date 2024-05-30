package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {
        try {
            configureDatabase();
        } catch (Exception e) {
            System.out.println("SQLAuthDAO threw an error while initializing");
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
                authToken VARCHAR(255) PRIMARY KEY,
                username VARCHAR(255),
                FOREIGN KEY (username) REFERENCES user(username)
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

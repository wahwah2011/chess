package dataaccess;

import model.GameData;
import model.GameList;
import model.JoinRequest;

import java.sql.SQLException;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() {
        try {
            configureDatabase();
        } catch (Exception e) {
            System.out.println("SQLGameDAO threw an error while initializing");
        }
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(JoinRequest joinRequest) throws DataAccessException {
        return null;
    }

    @Override
    public GameList listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData, JoinRequest joinRequest, String userName) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
                gameID INT AUTO_INCREMENT PRIMARY KEY,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255),
                game TEXT
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

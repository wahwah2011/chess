package dataaccess;

import model.AuthData;
import model.GameData;
import model.GameList;
import model.JoinRequest;

import java.sql.*;

import static java.sql.Types.NULL;

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
        String statement = "DELETE FROM game";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    if (param instanceof Integer p) ps.setInt(i+1, p);
                    else if (param instanceof GameData p) ps.setString(i + 1, p.toString());
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

package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SQLGameDAO extends SQLBaseDAO implements GameDAO {

    public SQLGameDAO() {
        try {
            configureDatabase(List.of(
                    """
                    CREATE TABLE IF NOT EXISTS game (
                        gameID INT AUTO_INCREMENT PRIMARY KEY,
                        whiteUsername VARCHAR(255),
                        blackUsername VARCHAR(255),
                        gameName VARCHAR(255),
                        game TEXT
                    );
                    """
            ));
        } catch (Exception e) {
            System.err.println("SQLGameDAO threw an error while initializing: " + e.getMessage());
        }
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        int gameID = new Random().nextInt(100000);
        String gameString = serializeGame(gameData.game());

        String statement = "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(statement, gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameString);

        return new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game(), null);
    }

    @Override
    public GameData getGame(JoinRequest joinRequest) throws DataAccessException {
        int gameID = joinRequest.gameID();
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(statement)) {

            stmt.setInt(1, gameID);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int dbGameID = rs.getInt("gameID");
                    String dbWhiteUsername = rs.getString("whiteUsername");
                    String dbBlackUsername = rs.getString("blackUsername");
                    String dbGameName = rs.getString("gameName");
                    String dbGame = rs.getString("game");
                    ChessGame deserializedGame = deserializeGame(dbGame);
                    return new GameData(dbGameID, dbWhiteUsername, dbBlackUsername, dbGameName, deserializedGame, null);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to get game");
        }
        throw new DataAccessException("Game ID doesn't exist");
    }

    @Override
    public GameList listGames() throws DataAccessException {
        ArrayList<GameData> list = new ArrayList<>();
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(statement);
             var rs = stmt.executeQuery()) {

            while (rs.next()) {
                int dbGameID = rs.getInt("gameID");
                String dbWhiteUsername = rs.getString("whiteUsername");
                String dbBlackUsername = rs.getString("blackUsername");
                String dbGameName = rs.getString("gameName");
                String dbGame = rs.getString("game");
                ChessGame deserializedGame = deserializeGame(dbGame);
                list.add(new GameData(dbGameID, dbWhiteUsername, dbBlackUsername, dbGameName, deserializedGame, null));
            }
            return new GameList(list, null);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to list games");
        }
    }

    @Override
    public void updateGame(GameData gameData, JoinRequest joinRequest, String userName) throws DataAccessException {
        ChessGame.TeamColor color = joinRequest.playerColor();
        int gameID = joinRequest.gameID();
        String statement;

        if (color == ChessGame.TeamColor.BLACK) {
            if (teamIsEmpty(gameData, color)) {
                statement = "UPDATE game SET blackUsername = ? WHERE gameID = ?";
            } else {
                throw new DataAccessException("Black team is already taken");
            }
        } else {
            if (teamIsEmpty(gameData, color)) {
                statement = "UPDATE game SET whiteUsername = ? WHERE gameID = ?";
            } else {
                throw new DataAccessException("White team is already taken");
            }
        }
        executeUpdate(statement, userName, gameID);
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "DELETE FROM game";
        executeUpdate(statement);
    }

    private boolean teamIsEmpty(GameData gameData, ChessGame.TeamColor color) {
        return color == ChessGame.TeamColor.BLACK ? gameData.blackUsername() == null : gameData.whiteUsername() == null;
    }

    private String serializeGame(ChessGame game) {
        return new Gson().toJson(game);
    }

    private ChessGame deserializeGame(String json) {
        return new Gson().fromJson(json, ChessGame.class);
    }
}

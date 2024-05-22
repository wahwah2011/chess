package dataaccess;

import model.GameData;
import model.GameList;

public interface GameDAO {
    void createGame(GameData gameData) throws DataAccessException;
    GameData getGame(GameData gameData) throws DataAccessException;
    GameList listGames() throws DataAccessException;
    void updateGame(GameData gameData, String userName) throws DataAccessException; // maybe don't need userName string?
    void clear() throws DataAccessException;
}

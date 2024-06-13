package dataaccess;

import model.GameData;
import model.GameList;
import model.JoinRequest;

public interface GameDAO {
    GameData createGame(GameData gameData) throws DataAccessException;
    GameData getGame(Integer gameID) throws DataAccessException;
    GameList listGames() throws DataAccessException;
    void updateGame(GameData gameData, JoinRequest joinRequest, String userName) throws DataAccessException; // maybe don't need username string?
    void clear() throws DataAccessException;
}

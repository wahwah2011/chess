package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemoryGameDAO implements GameDAO {
    //give this a private data structure type that can store information?
    private Set<GameData> games;

    @Override
    public void createGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public GameData getGame(GameData gameData) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData gameData, String userName) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}

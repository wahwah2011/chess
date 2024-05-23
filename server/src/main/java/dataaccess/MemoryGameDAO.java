package dataaccess;

import model.GameData;
import model.GameList;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MemoryGameDAO implements GameDAO {
    //give this a private data structure type that can store information?
    private HashSet<GameData> games = new HashSet<>();

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        games.add(gameData);
    }

    @Override
    public GameData getGame(GameData gameData) throws DataAccessException {
        if (games.contains(gameData)) return gameData;
        else return null;
    }

    @Override
    public GameList listGames() throws DataAccessException {
        GameList gameList = new GameList(games);
        return gameList;
    }

/*
    Updates a chess game. It should replace the chess game string corresponding to a given gameID.
    This is used when players join a game or when a move is made.
    Just replace the whole ChessGame object--you can make it a separate method if you want
*/
    @Override
    public void updateGame(GameData gameData, String userName) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryGameDAO that = (MemoryGameDAO) o;
        return Objects.equals(games, that.games);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(games);
    }
}

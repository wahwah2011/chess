package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameList;
import model.JoinRequest;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {
    //give this a private data structure type that can store information?
    private ArrayList<GameData> games = new ArrayList<>();

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        Random random = new Random();
        int gameID = random.nextInt(100000);

        GameData newGame = new GameData(gameID,null,null, gameData.gameName(),null,null);
        GameData returnGame = new GameData(gameID,null,null,null,null,null);

        games.add(newGame);

        return returnGame;
    }

    public void addGame(GameData gameData) {
        games.add(gameData);
    }

    @Override
    public GameList listGames() throws DataAccessException {
        GameList gameList = new GameList(games, null);
        return gameList;
    }

    @Override
    public GameData getGame(JoinRequest joinRequest) throws DataAccessException {
        GameData game = new GameData(null,null,null,null,null,null);
        int gameID = joinRequest.gameID();
        for (GameData g : games) {
            int currGameID = g.gameID();
            if (currGameID == gameID) {
                game = g;
            }
        }
        if (game.gameID() != null) {
            return game;
        }
        else throw new DataAccessException("you are lame, your game doesn't exist");
    }

    @Override
    public void updateGame(GameData game, JoinRequest joinRequest, String userName) throws DataAccessException {
        String color = joinRequest.playerColor();
        GameData updateGame;

        if (color.equals("BLACK")) {
            if (game.blackUsername() == null) {
                updateGame = updateTeamUser(game,joinRequest.playerColor(),userName);
                games.remove(game);
                games.add(updateGame);
            }
            else throw new DataAccessException("Black name already taken");
        }
        else if (color.equals("WHITE")) {
            if (game.whiteUsername() == null) {
                updateGame = updateTeamUser(game,joinRequest.playerColor(),userName);
                games.remove(game);
                games.add(updateGame);
            }
            else throw new DataAccessException("White name already taken");
        }
    }

    public GameData updateTeamUser(GameData gameData, String color, String userName) {
        int gameID = gameData.gameID();
        String blackUsername = gameData.blackUsername();
        String whiteUsername = gameData.whiteUsername();
        if ((Objects.equals(color, "BLACK")) && (blackUsername == null)) {
            blackUsername = userName;
        }
        else if ((Objects.equals(color, "WHITE")) && (whiteUsername == null)) {
            whiteUsername = userName;
        }
        String gameName = gameData.gameName();
        ChessGame game = gameData.game();
        String message = gameData.message();

        return new GameData(gameID,whiteUsername,blackUsername,gameName,game,message);
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

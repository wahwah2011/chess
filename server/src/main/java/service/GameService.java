package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.GameList;


public class GameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public GameList listGames(AuthData authorization) {
        try {
            authDAO.getAuth(authorization);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            return gameDAO.listGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //could also pass in a GameData object that only contains an initialized gameName
    public GameData createGame(AuthData authorization, GameData gameData) {
        try {
            authDAO.getAuth(authorization);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            //handle creation of gameID in handler?
            gameDAO.createGame(gameData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return gameData;
    }

    public void joinGame(AuthData authorization, GameData gameData) {}


}

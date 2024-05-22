package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.GameList;

import java.util.Collection;
import java.util.Set;
import java.util.Random;

public class GameService {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public GameList listGames(AuthData authorization) {
        try {
            authDAO.getAuth(authorization.userName());
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
            authDAO.getAuth(authorization.userName());
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

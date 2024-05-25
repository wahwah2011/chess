package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.GameList;


public class GameService extends Authorization {
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        super.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public GameList listGames(AuthData authorization) {
        AuthData authorized;
        GameList list;

        authorized = authorize(authorization);
        if (authorized.message() != null) {
            list = new GameList(null,authorized.message());
            return list;
        }

        try {
            list = gameDAO.listGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    //could also pass in a GameData object that only contains an initialized gameName
    public GameData createGame(AuthData authorization, GameData gameData) {
        AuthData authorized;
        GameData game;

        authorized = authorize(authorization);
        if (authorized.message() != null) {
            game = new GameData(null,null,null,null,null, authorized.message());
            return game;
        }

        try {
            return gameDAO.createGame(gameData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinGame(AuthData authorization, GameData gameData) {}


}

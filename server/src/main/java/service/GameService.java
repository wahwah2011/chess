package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.GameList;
import model.JoinRequest;


public class GameService extends Authorization {
    private final GameDAO gameDAO;

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

    public AuthData joinGame(AuthData authorization, JoinRequest joinRequest) {
        AuthData authorized;
        GameData game;
        ChessGame.TeamColor color = joinRequest.playerColor();

        authorized = authorize(authorization);
        if (authorized.message() != null) {
            return new AuthData(null,null,authorized.message());
        }

        String username = authorized.username();

        try {
            game = gameDAO.getGame(joinRequest);
        } catch (DataAccessException e) {
            return new AuthData(null,null,"Error: bad request");
        }

        try {
            gameDAO.updateGame(game, joinRequest, username);
        } catch (DataAccessException e) {
            return new AuthData(null,null, "Error: already taken");
        }

        return new AuthData(null,null,null);
    }
}

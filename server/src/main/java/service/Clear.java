package service;

import dataaccess.*;
import model.ClearResponse;

public class Clear {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public Clear(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public ClearResponse clearGame() {
        //access and clear each of the above data structures:
        //gameDAO
        try {
            gameDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        //authDAO
        try {
            authDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        //userDAO
        try {
            userDAO.clear();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return new ClearResponse(null);
    }
}

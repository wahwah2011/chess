package service;

import dataaccess.*;
import model.Response.ClearResponse;

public class Clear {
    private MemoryAuthDAO authDAO;
    private MemoryUserDAO userDAO;
    private MemoryGameDAO gameDAO;

    public Clear(MemoryAuthDAO authDAO, MemoryUserDAO userDAO, MemoryGameDAO gameDAO) {
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

        ClearResponse response = new ClearResponse();

        return response;
    }
}

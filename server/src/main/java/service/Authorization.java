package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;

public class Authorization {
    protected AuthDAO authDAO;
    //make GameService and UserService extend this;
    //this class should contain authorization code for requests

    public AuthData authorize(AuthData auth) {

        try {
            authDAO.getAuth(auth);
        } catch (DataAccessException e) {
            return new AuthData(null,null, "Error: unauthorized");
        }

        return auth;
    }
}

package service;
//recognizing what kind of error in here

import dataaccess.*;
import model.*;
import model.Response.Response;

import java.util.UUID;

public class UserService extends Authorization {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        super.authDAO = authDAO;
    }

    //done
    public AuthData register(UserData user) {
        AuthData authorization = createAuth(user);

        try {
            userDAO.getUser(user);
            authorization = new AuthData(null,null,"Error: already taken");
        } catch (DataAccessException e) {
            //should be null, therefore execute other code when fails
            try {
                userDAO.createUser(user);
            } catch (DataAccessException f) {
                authorization = new AuthData(null,null,"Error: already taken");
            }
            try {
                authDAO.createAuth(authorization);
            } catch (DataAccessException g) {
                authorization = new AuthData(null,null,"Error: bad request");
            }
        }

        return authorization;
    }

    public AuthData login(UserData user) {
        //user == username, password
        AuthData authorization = createAuth(user);

        try {
            userDAO.getUser(user);
        } catch (DataAccessException e) {
            authorization = new AuthData(null, null, "Error: unauthorized");
        }
        try {
            authDAO.createAuth(authorization);
        } catch (DataAccessException e) {
            authorization = new AuthData(null, null, "Error: unauthorized");
        }

        return authorization;
    }

    public AuthData logout(AuthData auth) {
        AuthData authorized;
        authorized = authorize(auth);
        if (authorized.message() != null) {
            return authorized;
        }

        try {
            authDAO.deleteAuth(authorized);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return new AuthData(null,null,null);
    }

    public AuthData createAuth(UserData user) {
        String userName = user.username();
        String authToken = UUID.randomUUID().toString();
        AuthData authorization = new AuthData(authToken,userName,null);

        return authorization;
    }
}
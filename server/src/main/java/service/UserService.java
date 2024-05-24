package service;
//recognizing what kind of error in here

import dataaccess.*;
import model.*;

import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
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

    public void logout(UserData user) {
        AuthData authorization = createAuth(user);
        try {
            authorization = authDAO.getAuth(authorization);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            authDAO.deleteAuth(authorization);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData createAuth(UserData user) {
        String userName = user.username();
        String authToken = UUID.randomUUID().toString();
        AuthData authorization = new AuthData(authToken,userName,null);

        return authorization;
    }
}
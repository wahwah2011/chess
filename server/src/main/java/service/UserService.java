package service;

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

    public AuthData register(UserData user) {
        AuthData authorization = createAuth(user);

        try {
            userDAO.getUser(user);
        } catch (DataAccessException e) {
            //should be null
        }
        try {
            userDAO.createUser(user);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            authDAO.createAuth(authorization);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return authorization;
    }

    public AuthData login(UserData user) {
        AuthData authorization = createAuth(user);

        try {
            userDAO.getUser(user);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        try {
            authDAO.createAuth(authorization);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return authorization;
    }

    public void logout(UserData user) {
        AuthData authorization = createAuth(user);
        try {
            authorization = authDAO.getAuth(user.userName());
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
        String userName = user.userName();
        String authToken = UUID.randomUUID().toString();
        AuthData authorization = new AuthData(authToken,userName);

        return authorization;
    }
}
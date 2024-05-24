package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    UserService userService;

    MemoryAuthDAO authDAO;
    MemoryUserDAO userDAO;
    AuthDAO expectedAuth;
    UserDAO expectedUser;

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        expectedAuth = new MemoryAuthDAO();
        expectedUser = new MemoryUserDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    void registerSuccess() {
        UserData user = new UserData("Henry", "12345", "henry@mail.com");
        userService.register(user);

        assertNotEquals(authDAO,expectedAuth);
        assertNotEquals(userDAO,expectedUser);
    }

    @Test
    void registerFail() {
        UserData user = new UserData("Henry", "12345", "henry@mail.com");
        AuthData auth1 = userService.register(user);
        AuthData auth2 = userService.register(user);

        assertNotEquals(auth1,auth2);
    }

    @Test
    void loginSuccess() {
        UserData user = new UserData("Henry", "12345", "henry@mail.com");

        AuthData regAuth = userService.register(user);
        AuthData loginAuth = userService.login(user);

        System.out.println("regAuth: " + regAuth.toString() + " \nloginAuth: " + loginAuth.toString());

        assertNotEquals(loginAuth, regAuth);
    }

    @Test
    void loginFail() {
        UserData user = new UserData("Henry", "12345", "henry@mail.com");
        AuthData loginAuth = userService.login(user);

        System.out.println("loginAuth: " + loginAuth.toString());

        String expectedMessage = "Error: unauthorized";
        assertTrue(loginAuth.message() == expectedMessage);

    }

    @Test
    void logout() {
    }
}
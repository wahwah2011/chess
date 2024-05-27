package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
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
        assertSame(loginAuth.message(), expectedMessage);

    }

    @Test
    void logoutSuccess() {
        UserData user = new UserData("Henry", "12345", "henry@mail.com");
        AuthData expected = new AuthData(null,null,null);

        AuthData regAuth = userService.register(user);
        AuthData loginAuth = userService.login(user);

        AuthData logout = new AuthData(loginAuth.authToken(), loginAuth.username(),null);

        AuthData actual = userService.logout(logout);

        System.out.println("logoutAuth: " + actual.toString());

        assertEquals(actual, expected);
    }

    @Test
    void logoutFail() {
        UserData user = new UserData("Henry", "12345", "henry@mail.com");
        AuthData falseUser = new AuthData("2134125412", "Henry", null);

        AuthData regAuth = userService.register(user);
        AuthData loginAuth = userService.login(user);

        AuthData falseLogout = userService.logout(falseUser);

        System.out.println("falseLogout: " + falseLogout.toString());

        String expectedMessage = "Error: unauthorized";
        assertSame(falseLogout.message(), expectedMessage);
    }
}
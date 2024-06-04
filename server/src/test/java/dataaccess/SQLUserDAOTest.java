package dataaccess;

import model.UserData;
import dataaccess.DatabaseManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest extends DAOTest {

    UserData existingUser = new UserData("ExistingUser","ExistingPassword", "existingmail@mail.com");

    @BeforeEach
    void setUp() throws DataAccessException {
        fullClear();
        userData.createUser(existingUser);
    }

    @Test
    void createUser() {
        int initialRowCount = getDatabaseRows();
        assertTrue(initialRowCount == 1, "There are a viable number of rows in your database");
    }

    @Test
    void createUserFail() throws DataAccessException {
        UserData copyUser = new UserData("ExistingUser", "fakePassword", "fakeemail@mail.com");

        assertThrows(DataAccessException.class, () -> userData.createUser(copyUser));
    }

    @Test
    void getUser() throws DataAccessException {
        String dbName = userData.getUser(existingUser).username();
        assertEquals(dbName, existingUser.username());
    }

    @Test
    void getUserFail() throws DataAccessException {
        fullClear();
        assertThrows(DataAccessException.class, () -> userData.getUser(existingUser));
    }

    @Test
    void clear() throws DataAccessException {
        fullClear();
    }
}
package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest extends DAOTest {

    UserData existingUser = new UserData("ExistingUser","ExistingPassword", "existingmail@mail.com");
    AuthData existingAuth = new AuthData("authToken", "ExistingUser", null);



    @BeforeEach
    void setUp() throws DataAccessException {
        fullClear();
        userData.createUser(existingUser);
        authData.createAuth(existingAuth);
    }

    @Test
    void createAuth() {
        int initialRowCount = dataRows();
        assertEquals(2, initialRowCount, "There are a viable number of rows in your database");
    }

    @Test
    void createAuthFail() throws DataAccessException {
        fullClear();
        assertThrows(DataAccessException.class, () -> authData.createAuth(existingAuth), "Username does not exist");
    }

    @Test
    void getAuth() throws DataAccessException {
        assertEquals(authData.getAuth(existingAuth).authToken(), existingAuth.authToken());
    }

    @Test
    void getAuthFail() {
        AuthData falseData = new AuthData("fakeToken", "ExistingUser", null);
        assertThrows(DataAccessException.class, () -> authData.getAuth(falseData));
    }

    @Test
    void deleteAuth() throws DataAccessException {
        authData.deleteAuth(existingAuth);
        int postCount = dataRows();
        // should be one because originally 2, but row in authData was deleted
        assertEquals(1, postCount);
    }

    @Test
    void deleteAuthFail() throws DataAccessException {
        AuthData falseData = new AuthData("fakeToken", "ExistingUser", null);
        authData.deleteAuth(falseData);
        int postCount = dataRows();
        assertEquals(2,postCount);
    }

    @Test
    void clear() throws DataAccessException {
        fullClear();
    }
}
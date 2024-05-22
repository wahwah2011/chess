package dataaccess;

import model.UserData;

import java.util.Map;
import java.util.Set;

public class MemoryUserDAO implements UserDAO {

    private Set<UserData> users;

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (users.contains(userData)) {
            throw new DataAccessException("Error: already taken");
        }
        else users.add(userData);
    }

    @Override
    public UserData getUser(UserData userData) throws DataAccessException {
        if (!users.contains(userData)) {
            return null;
        }
        else return userData;
    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }
}

package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MemoryUserDAO implements UserDAO {

    private HashSet<UserData> users = new HashSet<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (!users.isEmpty()) {
            if (users.contains(userData)) {
                throw new DataAccessException("Error: already taken");
            }
            else users.add(userData);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryUserDAO that = (MemoryUserDAO) o;
        return Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(users);
    }
}

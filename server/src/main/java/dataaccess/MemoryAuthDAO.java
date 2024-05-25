package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MemoryAuthDAO implements AuthDAO {

    private HashSet<AuthData> auth = new HashSet<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        if (auth.contains(authData)) {
            throw new DataAccessException("Auth already exists");
        }
        else auth.add(authData);
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        String authorization = authData.authToken();
        boolean exists = false;

        for (AuthData a : auth) {
            if (a.authToken().equals(authorization)) {
                exists = true;
                break;
            }
        }
        if(exists) {
            return authData;
        }
        else {
            throw new DataAccessException("Not authorized");
        }
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        String authorization = authData.authToken();
        for (AuthData a : auth) {
            if (a.authToken().equals(authorization)) {
                auth.remove(a);
                break;
            }
        }
    }

    @Override
    public void clear() throws DataAccessException {
        auth.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemoryAuthDAO that = (MemoryAuthDAO) o;
        return Objects.equals(auth, that.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(auth);
    }
}

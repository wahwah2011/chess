package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public AuthData getAuth(String userName) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}

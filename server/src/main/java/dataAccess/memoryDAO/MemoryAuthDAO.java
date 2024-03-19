package dataAccess.memoryDAO;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthModel;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private static HashMap<String, String> authTokens = new HashMap<>();

    @Override
    public void createAuth(AuthModel newAuth) throws DataAccessException {
        authTokens.put(newAuth.authToken(), newAuth.username());
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authTokens.get(authToken) == null) {
            throw new DataAccessException("Not valid authToken");
        } else {
            authTokens.remove(authToken);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        authTokens.clear();
    }

    @Override
    public String checkValidAuth(String authToken) throws DataAccessException{
        String username = authTokens.get(authToken);
        if (username == null) {
            throw new DataAccessException("Not valid authtoken");
        } else {
            return username;
        }
    }
}

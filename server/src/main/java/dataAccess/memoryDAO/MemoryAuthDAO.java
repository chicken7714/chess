package dataAccess.memoryDAO;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthModel;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static HashMap<UUID, String> authTokens = new HashMap<>();

    @Override
    public void createAuth(AuthModel newAuth) {
        authTokens.put(newAuth.authToken(), newAuth.username());
    }

    @Override
    public void deleteAuth(UUID authToken) throws DataAccessException {
        if (authTokens.get(authToken) == null) {
            throw new DataAccessException("Not valid authToken");
        } else {
            authTokens.remove(authToken);
        }
    }

    @Override
    public void clear() {
        authTokens.clear();
    }

    @Override
    public String checkValidAuth(UUID authToken) throws DataAccessException{
        String username = authTokens.get(authToken);
        if (username == null) {
            throw new DataAccessException("Not valid authtoken");
        } else {
            return username;
        }
    }
}

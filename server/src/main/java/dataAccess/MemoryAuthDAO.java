package dataAccess;

import model.AuthModel;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    private static HashMap<String, String> authTokens = new HashMap<>();

    @Override
    public void createAuth(AuthModel newAuth) {
        authTokens.put(newAuth.username(), newAuth.authToken());
    }

    @Override
    public boolean getAuth(AuthModel auth) throws DataAccessException {
        if (authTokens.get(auth.username()) != null) {
            return true;
        } else {
            throw new DataAccessException("Auth token not found");
        }
    }

    @Override
    public void deleteAuth(AuthModel auth) {
        authTokens.remove(auth.username());
    }

    @Override
    public void clear() {
        authTokens.clear();
    }
}

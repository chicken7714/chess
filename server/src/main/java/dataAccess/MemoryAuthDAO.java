package dataAccess;

import model.AuthModel;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static HashMap<String, UUID> authTokens = new HashMap<>();

    @Override
    public void createAuth(AuthModel newAuth) {
        authTokens.put(newAuth.username(), newAuth.authToken());
    }

    @Override
    public UUID getAuth(String username) throws DataAccessException {
        var authToken = authTokens.get(username);
        if (authToken == null) {
            throw new DataAccessException("Auth Token not found");
        } else {
            return authToken;
        }
    }

    @Override
    public void deleteAuth(UUID authToken) throws DataAccessException {
        String usernameToBeDeleted = null;
        for (String username : authTokens.keySet()) {
            if (authTokens.get(username).equals(authToken)) {
                usernameToBeDeleted = username;
            }
        }
        if (usernameToBeDeleted == null) {
            throw new DataAccessException("User not found");
        }
        authTokens.remove(usernameToBeDeleted);
    }

    @Override
    public void clear() {
        authTokens.clear();
    }

    public String checkValidAuth(UUID authToken) throws DataAccessException{
        String matchedUser = null;
        for (String user : authTokens.keySet()) {
            if (authTokens.get(user).equals(authToken)) {
                return user;
            }
        }
        throw new DataAccessException("Auth not found");
    }
}

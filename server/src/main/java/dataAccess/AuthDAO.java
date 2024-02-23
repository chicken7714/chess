package dataAccess;

import model.AuthModel;

import java.util.UUID;

public interface AuthDAO {
    void createAuth(AuthModel newAuth);
    UUID getAuth(String username) throws DataAccessException;

    void deleteAuth(UUID authToken) throws DataAccessException;

    void clear();
}

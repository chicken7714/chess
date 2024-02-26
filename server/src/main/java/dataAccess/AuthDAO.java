package dataAccess;

import model.AuthModel;

import java.util.UUID;

public interface AuthDAO {
    void createAuth(AuthModel newAuth);
    void deleteAuth(UUID authToken) throws DataAccessException;
    void clear();
    String checkValidAuth(UUID authToken) throws DataAccessException;
}

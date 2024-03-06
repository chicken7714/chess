package dataAccess;

import model.AuthModel;

import java.util.UUID;

public interface AuthDAO {
    void createAuth(AuthModel newAuth) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
    String checkValidAuth(String authToken) throws DataAccessException;
}

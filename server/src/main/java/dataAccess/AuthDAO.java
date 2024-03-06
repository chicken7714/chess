package dataAccess;

import model.AuthModel;
import service.UnauthorizedAccessException;

import java.util.UUID;

public interface AuthDAO {
    void createAuth(AuthModel newAuth) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException, UnauthorizedAccessException;
    void clear() throws DataAccessException;
    String checkValidAuth(String authToken) throws DataAccessException;
}

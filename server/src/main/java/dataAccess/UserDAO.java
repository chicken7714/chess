package dataAccess;

import model.UserModel;

public interface UserDAO {
    void createUser(UserModel user) throws DataAccessException;
    void clear() throws DataAccessException;
    boolean isValidUser(String username, String password) throws DataAccessException;
    boolean isInDatabase(String username) throws DataAccessException;
}

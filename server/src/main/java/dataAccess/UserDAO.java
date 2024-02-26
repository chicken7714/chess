package dataAccess;

import model.UserModel;

public interface UserDAO {
    void createUser(UserModel user);
    void clear();
    boolean isValidUser(String username, String password) throws DataAccessException;
    boolean isInDatabase(String username);
}

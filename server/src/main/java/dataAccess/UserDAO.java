package dataAccess;

import model.UserModel;

public interface UserDAO {
    void createUser(UserModel user) throws DataAccessException;
    UserModel getUser(String username) throws DataAccessException;
    void clear();
}

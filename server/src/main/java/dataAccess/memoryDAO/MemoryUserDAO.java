package dataAccess.memoryDAO;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserModel;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private static HashMap<String, UserModel> users = new HashMap<>();

    @Override
    public void createUser(UserModel user) throws DataAccessException {
        users.put(user.username(), user);
    }

    @Override
    public void clear() throws DataAccessException {
        users.clear();
    }

    @Override
    public boolean isValidUser(String username, String password) throws DataAccessException {
        var userModel = users.get(username);
        if (userModel == null) {
            throw new DataAccessException("Username doesn't exist");
        }

        if (password.equals(userModel.password())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isInDatabase(String username) throws DataAccessException {
        if (users.get(username) != null) {
            return true;
        } else {
            return false;
        }
    }
}

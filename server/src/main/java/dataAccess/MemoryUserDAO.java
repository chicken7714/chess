package dataAccess;

import model.UserModel;

import javax.xml.crypto.Data;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private static HashMap<String, UserModel> users = new HashMap<>();

    @Override
    public void createUser(UserModel user) {
        users.put(user.username(), user);
    }
    @Override
    public UserModel getUser(String username) throws DataAccessException {
        return users.get(username);
    }

    @Override
    public void clear() {
        users.clear();
    }

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

    public boolean isInDatabase(String username) {
        if (users.get(username) != null) {
            return true;
        } else {
            return false;
        }
    }
}

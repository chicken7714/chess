package dataAccess;

import model.UserModel;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private static HashMap<String, UserModel> users = new HashMap<>();

    @Override
    public void createUser(UserModel user) throws DataAccessException {
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
}

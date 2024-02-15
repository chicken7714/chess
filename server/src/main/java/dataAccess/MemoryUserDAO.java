package dataAccess;

import model.UserModel;

import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {
    private static HashSet<UserModel> users = new HashSet<>();

    @Override
    public void createUser(UserModel user) throws DataAccessException {
        users.add(user);
    }

    @Override
    public UserModel getUser(String username) throws DataAccessException {
        for (UserModel user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("User not Found!");
    }

    @Override
    public void clear() {
        users.clear();
    }
}

package dataAccess;

import model.AuthModel;

public interface AuthDAO {
    void createAuth(AuthModel newAuth);
    boolean getAuth(AuthModel auth) throws DataAccessException;
    void deleteAuth(AuthModel auth);

    void clear();
}

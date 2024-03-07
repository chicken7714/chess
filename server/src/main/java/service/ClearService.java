package service;

import dataAccess.*;

public class ClearService {

    public boolean clear() throws DataAccessException{
        clearGame();
        clearAuth();
        clearUser();
        return true;
    }
    private void clearGame() throws DataAccessException {
        var gameDao = new SQLGameDAO();
        gameDao.clear();
    }

    private void clearAuth() throws DataAccessException {
        var authDao = new SQLAuthDAO();
        authDao.clear();
    }

    private void clearUser() throws DataAccessException {
        var userDao = new SQLUserDAO();
        userDao.clear();
    }
}

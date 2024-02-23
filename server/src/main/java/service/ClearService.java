package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class ClearService {

    public boolean clear() {
        clearGame();
        clearAuth();
        clearUser();
        return true;
    }
    private void clearGame() {
        var gameDao = new MemoryGameDAO();
        gameDao.clear();
    }

    private void clearAuth() {
        var authDao = new MemoryAuthDAO();
        authDao.clear();
    }

    private void clearUser() {
        var userDao = new MemoryUserDAO();
        userDao.clear();
    }
}

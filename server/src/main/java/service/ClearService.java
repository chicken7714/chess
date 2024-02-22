package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import requestresponse.ClearRequest;
import requestresponse.ClearResponse;

public class ClearService {

    public ClearResponse clear(ClearRequest request) {
        if (request.clearAll()) {
            clearGame();
            clearAuth();
            clearUser();
            return new ClearResponse(true);
        }
        return new ClearResponse(false);
    }
    void clearGame() {
        var gameDao = new MemoryGameDAO();
        gameDao.clear();
    }

    void clearAuth() {
        var authDao = new MemoryAuthDAO();
        authDao.clear();
    }

    void clearUser() {
        var userDao = new MemoryUserDAO();
        userDao.clear();
    }
}

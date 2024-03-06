package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.memoryDAO.MemoryAuthDAO;
import request.LogoutRequest;

import javax.xml.crypto.Data;

public class LogoutService {

    public boolean logout(LogoutRequest request) throws UnauthorizedAccessException, DataAccessException{
        String authToken = request.authToken();
        AuthDAO authDAO = new SQLAuthDAO();

        authDAO.deleteAuth(authToken);
        return true;
    }
}

package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.memoryDAO.MemoryAuthDAO;
import request.LogoutRequest;

public class LogoutService {

    public boolean logout(LogoutRequest request) throws UnauthorizedAccessException, DataAccessException{
        String authToken = request.authToken();
        AuthDAO authDAO = new SQLAuthDAO();

        try {
            authDAO.deleteAuth(authToken);
            return true;
        } catch (DataAccessException e) {
            throw new UnauthorizedAccessException("Error: unauthorized");
        }
    }
}

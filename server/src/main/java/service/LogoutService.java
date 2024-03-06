package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.memoryDAO.MemoryAuthDAO;
import request.LogoutRequest;

import java.util.UUID;

public class LogoutService {

    public boolean logout(LogoutRequest request) throws UnauthorizedAccessException {
        String authToken = request.authToken();
        AuthDAO authDAO = new MemoryAuthDAO();

        try {
            authDAO.deleteAuth(authToken);
            return true;
        } catch (DataAccessException e) {
            throw new UnauthorizedAccessException("Error: unauthorized");
        }
    }
}

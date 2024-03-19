package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import request.LogoutRequest;

public class LogoutService {

    public boolean logout(LogoutRequest request) throws UnauthorizedAccessException, DataAccessException{
        String authToken = request.authToken();
        AuthDAO authDAO = new SQLAuthDAO();

        authDAO.deleteAuth(authToken);
        return true;
    }
}

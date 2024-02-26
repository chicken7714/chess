package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthModel;
import request.LoginRequest;
import response.LoginResponse;

import java.util.UUID;

public class LoginService {

    public LoginResponse getUser(LoginRequest request) throws UnauthorizedAccessException, RuntimeException {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();

        boolean isValidUser;
        try {
            isValidUser = userDAO.isValidUser(request.username(), request.password());
        } catch (DataAccessException e) {
            throw new RuntimeException("Username not found");
        }

        if (!isValidUser) {
            throw new UnauthorizedAccessException("Password not valid");
        } else {
            UUID authToken = generateAuthToken();
            AuthModel authData = new AuthModel(authToken, request.username());
            authDAO.createAuth(authData);
            return new LoginResponse(request.username(), authToken);
        }
    }

    private UUID generateAuthToken() {
        return UUID.randomUUID();
    }
}

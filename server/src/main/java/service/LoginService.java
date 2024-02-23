package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthModel;
import requestresponse.LoginRequest;
import requestresponse.LoginResponse;

import javax.xml.crypto.Data;
import java.util.UUID;

public class LoginService {

    public LoginResponse getUser(LoginRequest request) throws UnauthorizedAccessException, RuntimeException {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();

        boolean isValidUser = false;
        try {
            isValidUser = userDAO.isValidUser(request.username(), request.password());
        } catch (DataAccessException e) {
            throw new RuntimeException("Username not found");
        }

        if (!isValidUser) {
            throw new UnauthorizedAccessException("Password not valid");
        } else {
            UUID authToken;
            try {
                authToken = authDAO.getAuth(request.username());

            } catch (DataAccessException exception) {
                authToken = generateAuthToken();
            }
            AuthModel authData = new AuthModel(authToken, request.password());
            authDAO.createAuth(authData);
            return new LoginResponse(request.username(), authToken);
        }
    }

    private UUID generateAuthToken() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }
}

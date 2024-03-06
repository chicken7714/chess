package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;

import model.AuthModel;
import model.UserModel;
import request.RegisterRequest;
import response.RegisterResponse;

import java.util.UUID;

public class RegisterService {

    public RegisterResponse registerUser(RegisterRequest request) throws UnauthorizedAccessException,
            InvalidRequestException {
        try {
            var userDAO = new SQLUserDAO();
            var authDAO = new SQLAuthDAO();

            if (!checkValidRequest(request)) {
                throw new InvalidRequestException("Error: Invalid Request");
            }
            try {
                if (userDAO.isInDatabase(request.username())) {
                    throw new UnauthorizedAccessException("Error: already taken");
                } else {
                    createUser(request.username(), request.password(), request.email());

                    String authToken = generateAuthToken();
                    authDAO.createAuth(new AuthModel(authToken, request.username()));

                    return new RegisterResponse(request.username(), authToken);
                }
            } catch (DataAccessException e) {
                throw new InvalidRequestException(e.getMessage());
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private boolean checkValidRequest(RegisterRequest request) {
        if ((request.username() == null) || (request.password() == null) || (request.email() == null)) {
            return false;
        } else {
            return true;
        }
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

    private void createUser(String username, String password, String email) throws DataAccessException {
        var userDAO = new SQLUserDAO();
        UserModel user = new UserModel(username, password, email);
        userDAO.createUser(user);
    }
}

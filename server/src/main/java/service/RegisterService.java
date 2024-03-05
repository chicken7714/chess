package service;

import dataAccess.memoryDAO.MemoryAuthDAO;
import dataAccess.memoryDAO.MemoryUserDAO;
import model.AuthModel;
import model.UserModel;
import request.RegisterRequest;
import response.RegisterResponse;

import java.util.UUID;

public class RegisterService {

    public RegisterResponse registerUser(RegisterRequest request) throws UnauthorizedAccessException,
                                                                         InvalidRequestException {
        var userDAO = new MemoryUserDAO();
        var authDAO = new MemoryAuthDAO();

        if (!checkValidRequest(request)) {
            throw new InvalidRequestException("Error: Invalid Request");
        }
        if (userDAO.isInDatabase(request.username())) {
            throw new UnauthorizedAccessException("Error: already taken");
        } else {
            createUser(request.username(), request.password(), request.email());

            UUID authToken = generateAuthToken();
            authDAO.createAuth(new AuthModel(authToken, request.username()));

            return new RegisterResponse(request.username(), authToken);
        }

    }

    private boolean checkValidRequest(RegisterRequest request) {
        if ((request.username() == null) || (request.password() == null) || (request.email() == null)) {
            return false;
        } else {
            return true;
        }
    }

    private UUID generateAuthToken() {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    private void createUser(String username, String password, String email) {
        var userDAO = new MemoryUserDAO();
        UserModel user = new UserModel(username, password, email);
        userDAO.createUser(user);
    }
}

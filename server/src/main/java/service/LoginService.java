package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;
import dataAccess.memoryDAO.MemoryAuthDAO;
import dataAccess.memoryDAO.MemoryUserDAO;
import model.AuthModel;
import request.LoginRequest;
import response.LoginResponse;

import java.util.UUID;

public class LoginService {

    public LoginResponse getUser(LoginRequest request) throws UnauthorizedAccessException, RuntimeException {
        //Creates DAOs;
        //Sees if username is found
        //Sees if password is valid
        //generates authtoken and inserts it into AuthDAO
        //Returns response

        //try {
        try {
            var userDAO = new SQLUserDAO();
            var authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        return new LoginResponse("Hello", "World");

//            boolean isValidUser;
//            try {
//                isValidUser = userDAO.isValidUser(request.username(), request.password());
//            } catch (DataAccessException e) {
//                throw new RuntimeException("Username not found");
//            }
//
//            if (!isValidUser) {
//                throw new UnauthorizedAccessException("Password not valid");
//            } else {
//                String authToken = generateAuthToken();
//                AuthModel authData = new AuthModel(authToken, request.username());
//
//                try {
//                    authDAO.createAuth(authData);
//                    return new LoginResponse(request.username(), authToken);
//                } catch (DataAccessException e) {
//                    throw new RuntimeException("SQL Error");
//                }
//            }
//        } catch (DataAccessException e) {
//            throw new RuntimeException("SQL Error");
//        }
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}

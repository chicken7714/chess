package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;

public class WebSocketServices {

    public String getUsername(String authToken) throws DataAccessException {
        SQLAuthDAO authDAO = new SQLAuthDAO();
        return authDAO.checkValidAuth(authToken);
    }
}

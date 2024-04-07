package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.GameModel;

public class WebSocketServices {

    public String getUsername(String authToken) throws DataAccessException {
        SQLAuthDAO authDAO = new SQLAuthDAO();
        return authDAO.checkValidAuth(authToken);
    }

    public void updateGame(int gameID, String gameJson) throws DataAccessException {
        SQLGameDAO gameDAO = new SQLGameDAO();
        gameDAO.updateGame(gameID, gameJson);
    }

    public String getGame(int gameID) throws InvalidRequestException {
        try {
            SQLGameDAO gameDAO = new SQLGameDAO();
            return gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            throw new InvalidRequestException("Game not found");
        }
    }
}

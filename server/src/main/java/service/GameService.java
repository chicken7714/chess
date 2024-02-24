package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.GameModel;
import requestresponse.ListGameRequest;
import requestresponse.ListGameResponse;

import java.util.Collection;
import java.util.UUID;

public class GameService {

    public ListGameResponse listGames(ListGameRequest request) throws UnauthorizedAccessException {
        var authDAO = new MemoryAuthDAO();
        var gamesDAO = new MemoryGameDAO();
        UUID authToken = request.authToken();

        try {
            authDAO.checkValidAuth(authToken);
            Collection<GameModel> games = gamesDAO.listGames();
            return new ListGameResponse(games);
        } catch (DataAccessException e) {
            throw new UnauthorizedAccessException("Unauthorized");
        }
    }
}

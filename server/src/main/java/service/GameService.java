package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.GameModel;
import requestresponse.*;

import javax.xml.crypto.Data;
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

    public CreateGameResponse createGame(CreateGameRequest request) throws UnauthorizedAccessException, InvalidRequestException {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        UUID authToken = request.authToken();

        try {
            authDAO.checkValidAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedAccessException("Unauthorized");
        }

        int gameID = gameDAO.createGame(request.gameName());
        return new CreateGameResponse(gameID);
    }

    public void joinGame(JoinGameRequest request) throws UnauthorizedAccessException, InvalidRequestException,
                                                         UnavailableRequestException {
        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();
        UUID authToken = request.authToken();
        String username;

        try {
            username = authDAO.checkValidAuth(authToken);
        } catch (DataAccessException e) {
            throw new UnauthorizedAccessException("Unauthorized");
        }

        if ((!request.playerColor().equals("WHITE")) && (!request.playerColor().equals("BLACK"))) {
            throw new InvalidRequestException("Improper Color Provided");
        }

        try {
            gameDAO.addUser(request.gameID(), username, request.playerColor());
        } catch (DataAccessException e) {
            throw new UnavailableRequestException("Player color already taken");
        }
    }
}

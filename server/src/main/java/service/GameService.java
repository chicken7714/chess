package service;

import dataAccess.DataAccessException;
import dataAccess.memoryDAO.MemoryAuthDAO;
import dataAccess.memoryDAO.MemoryGameDAO;
import model.GameModel;
import request.*;
import response.CreateGameResponse;
import response.ListGameResponse;

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

        try {
            gameDAO.isValidGame(request.gameID());
        } catch (DataAccessException e) {
            throw new InvalidRequestException("Bad Game ID");
        }

        if (request.playerColor() == null) {
            // User gets added as a spectator
        } else if ((request.playerColor().equals("WHITE")) || (request.playerColor().equals("BLACK"))) {
            try {
                gameDAO.addUser(request.gameID(), username, request.playerColor());
            } catch (DataAccessException e) {
                throw new UnavailableRequestException("Player color already taken");
            }
        }
    }
}

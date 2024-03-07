package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.GameModel;
import request.*;
import response.CreateGameResponse;
import response.ListGameResponse;

import java.util.Collection;

public class GameService {

    public ListGameResponse listGames(ListGameRequest request) throws UnauthorizedAccessException, DataAccessException {
        var authDAO = new SQLAuthDAO();
        var gamesDAO = new SQLGameDAO();
        String authToken = request.authToken();

        String username = authDAO.checkValidAuth(authToken);
        if (username == null) {
            throw new UnauthorizedAccessException("Unauthorized");
        }
        try {
            Collection<GameModel> games = gamesDAO.listGames();
            return new ListGameResponse(games);
        } catch (DataAccessException e) {
            throw new UnauthorizedAccessException("Unauthorized");
        }
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws UnauthorizedAccessException,
                                                                           InvalidRequestException, DataAccessException {
        var authDAO = new SQLAuthDAO();
        var gameDAO = new SQLGameDAO();
        String authToken = request.authToken();

        String username = authDAO.checkValidAuth(authToken);
        if (username == null) {
            throw new UnauthorizedAccessException("Unauthorized");
        }

        try {
            int gameID = gameDAO.createGame(request.gameName());
            return new CreateGameResponse(gameID);
        } catch (DataAccessException e) {
            throw new InvalidRequestException(e.getMessage());
        }
    }

    public void joinGame(JoinGameRequest request) throws UnauthorizedAccessException, InvalidRequestException,
            UnavailableRequestException, DataAccessException {
        var gameDAO = new SQLGameDAO();
        var authDAO = new SQLAuthDAO();
        String authToken = request.authToken();
        String username;

        username = authDAO.checkValidAuth(authToken);
        if (username==null) {
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

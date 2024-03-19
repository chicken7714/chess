package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.*;
import response.CreateGameResponse;
import response.ErrorResponse;
import response.ListGameResponse;
import service.GameService;
import service.InvalidRequestException;
import service.UnauthorizedAccessException;
import service.UnavailableRequestException;
import spark.Request;
import spark.Response;

public class GameHandler {

    public Object listGames(Request req, Response res) {
        var gson = new Gson();
        String authToken = req.headers("authorization");
        ListGameRequest listGameRequest = new ListGameRequest(authToken);
        System.out.println(listGameRequest.authToken());

        GameService gameService = new GameService();
        try {
            ListGameResponse listGameResponse = gameService.listGames(listGameRequest);
            System.out.println("GAMES");
            System.out.println(listGameResponse.toString());
            res.status(200);
            return gson.toJson(listGameResponse);
        } catch (UnauthorizedAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: 500"));
        }
    }

    public Object createGame(Request req, Response res) {
        var gson = new Gson();
        String authToken = req.headers("authorization");
        var gameName = gson.fromJson(req.body(), CreateGameData.class);

        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName.gameName());

        GameService service = new GameService();
        try {
            CreateGameResponse createGameResponse = service.createGame(createGameRequest);
            res.status(200);
            return gson.toJson(createGameResponse);
        } catch (UnauthorizedAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (InvalidRequestException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: 500"));
        }
    }

    public Object joinGame(Request req, Response res) {
        var gson = new Gson();
        String authToken;
        try {
            authToken = req.headers("authorization");
        } catch (IllegalArgumentException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        }

        var requestBody = gson.fromJson(req.body(), JoinGameData.class);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, requestBody.playerColor(), requestBody.gameID());
        GameService service = new GameService();

        try {
            service.joinGame(joinGameRequest);
            res.status(200);
            return "{}";
        } catch (UnauthorizedAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (InvalidRequestException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (UnavailableRequestException e) {
            res.status(403);
            return gson.toJson(new ErrorResponse("Error: already taken"));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Error: 500"));
        }

    }

}

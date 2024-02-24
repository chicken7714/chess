package handler;

import com.google.gson.Gson;
import requestresponse.*;
import service.GameService;
import service.InvalidRequestException;
import service.UnauthorizedAccessException;
import service.UnavailableRequestException;
import spark.Request;
import spark.Response;

import java.util.UUID;

public class GameHandler {

    public Object listGames(Request req, Response res) {
        var gson = new Gson();
        UUID authToken = UUID.fromString(req.headers("authorization"));
        ListGameRequest listGameRequest = new ListGameRequest(authToken);

        GameService gameService = new GameService();
        try {
            ListGameResponse listGameResponse = gameService.listGames(listGameRequest);
            res.status(200);
            return gson.toJson(listGameResponse);
        } catch (UnauthorizedAccessException e) {
            res.status(401);
            return new ErrorResponse("Error: unauthorized");
        }
    }

    public Object createGame(Request req, Response res) {
        var gson = new Gson();
        UUID authToken = UUID.fromString(req.headers("authorization"));
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
        }
    }

    public Object joinGame(Request req, Response res) {
        var gson = new Gson();
        UUID authToken;
        try {
            authToken = UUID.fromString(req.headers("authorization"));
        } catch (IllegalArgumentException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        }

        var requestBody = gson.fromJson(req.body(), JoinGameData.class);
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, requestBody.playerColor(), requestBody.gameID());
        System.out.println(joinGameRequest);
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
        }

    }

}

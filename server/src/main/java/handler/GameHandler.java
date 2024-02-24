package handler;

import com.google.gson.Gson;
import requestresponse.ErrorResponse;
import requestresponse.ListGameRequest;
import requestresponse.ListGameResponse;
import service.GameService;
import service.UnauthorizedAccessException;
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
            return gson.toJson(listGameResponse);
        } catch (UnauthorizedAccessException e) {
            res.status(401);
            return new ErrorResponse("Error: unauthorized");
        }
    }

}

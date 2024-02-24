package handler;

import com.google.gson.Gson;
import requestresponse.ErrorResponse;
import requestresponse.LogoutRequest;
import service.LogoutService;
import service.UnauthorizedAccessException;
import spark.Request;
import spark.Response;

import java.util.UUID;

public class LogoutHandler {

    public Object logout(Request req, Response res) {
        var gson = new Gson();
        UUID authToken = UUID.fromString(req.headers("authorization"));
        LogoutRequest logoutRequest = new LogoutRequest(authToken);

        LogoutService service = new LogoutService();
        try {
            service.logout(logoutRequest);
            res.status(200);
            return "{}";
        } catch (UnauthorizedAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}

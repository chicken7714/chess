package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import response.ErrorResponse;
import request.LogoutRequest;
import service.LogoutService;
import service.UnauthorizedAccessException;
import spark.Request;
import spark.Response;

public class LogoutHandler {

    public Object logout(Request req, Response res) {
        var gson = new Gson();
        String authToken = req.headers("authorization");
        LogoutRequest logoutRequest = new LogoutRequest(authToken);

        LogoutService service = new LogoutService();
        try {
            service.logout(logoutRequest);
            res.status(200);
            return "{}";
        } catch (UnauthorizedAccessException e) {
            System.out.println("Setting status to 401");
            res.status(401);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}

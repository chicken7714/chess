package handler;

import com.google.gson.Gson;
import response.ErrorResponse;
import request.LoginRequest;
import service.LoginService;
import service.UnauthorizedAccessException;
import spark.Request;
import spark.Response;

public class LoginHandler {

    public Object login(Request req, Response res) {
        var gson = new Gson();
        var user = gson.fromJson(req.body(), LoginRequest.class);
        LoginService service = new LoginService();

        try {
            service.getUser(user);
            res.status(200);
            return gson.toJson(service.getUser(user));
        } catch (UnauthorizedAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (RuntimeException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: Username not found"));
        }
    }
}

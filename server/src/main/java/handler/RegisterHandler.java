package handler;

import com.google.gson.Gson;
import response.ErrorResponse;
import request.RegisterRequest;
import response.RegisterResponse;
import service.InvalidRequestException;
import service.RegisterService;
import service.UnauthorizedAccessException;
import spark.Request;
import spark.Response;

public class RegisterHandler {

    public Object register(Request req, Response res) {
        var gson = new Gson();
        var user = gson.fromJson(req.body(), RegisterRequest.class);

        try {
            RegisterService service = new RegisterService();
            RegisterResponse response = service.registerUser(user);
            res.status(200);
            return gson.toJson(response);
        } catch (UnauthorizedAccessException e) {
            res.status(403);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (InvalidRequestException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}

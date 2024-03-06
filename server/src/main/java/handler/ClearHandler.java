package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import response.ErrorResponse;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object clear(Request req, Response res) {
        var serializer = new Gson();
        ClearService service = new ClearService();

        try {
            service.clear();
            return "{}";
        } catch (DataAccessException e) {
            res.status(500);
            return serializer.toJson(new ErrorResponse(e.getMessage()));
        }
    }
}

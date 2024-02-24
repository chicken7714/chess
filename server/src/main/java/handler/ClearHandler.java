package handler;

import com.google.gson.Gson;
import requestresponse.ErrorResponse;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object clear(Request req, Response res) {
        var serializer = new Gson();
        ClearService service = new ClearService();

        if (service.clear()) {
            res.status(200);
            return "{}";
        }
        else {
            res.status(500);
            return serializer.toJson(new ErrorResponse("Error: Unknown"));
        }
    }

}

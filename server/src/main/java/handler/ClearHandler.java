package handler;

import com.google.gson.Gson;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {

    public Object clear(Request req, Response res) {
        ClearService service = new ClearService();
        if (service.clear()) {
            res.status(200);
        }
        else {
            res.status(500);
        }
        var serializer = new Gson();
        var json = serializer.toJson("");
        return json;
    }

}

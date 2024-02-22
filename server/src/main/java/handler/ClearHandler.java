package handler;

import com.google.gson.Gson;
import requestresponse.ClearRequest;
import requestresponse.ClearResponse;
import service.ClearService;

public class ClearHandler {

    String clear() {
        var serializer = new Gson();

        ClearService service = new ClearService();
        ClearRequest request = new ClearRequest(true);
        ClearResponse response = service.clear(request);

        return serializer.toJson(response);
    }

}

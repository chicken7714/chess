package ServerFacade;

import com.google.gson.Gson;
import model.GameModel;
import model.UserModel;
import request.*;
import response.CreateGameResponse;
import response.ListGameResponse;
import response.LoginResponse;
import response.RegisterResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {

    private final String serverURL;
    private String authToken;

    public ServerFacade(String url) {
        this.serverURL = url;
    }

    public RegisterResponse registerUser(UserModel user) throws ResponseException {
        var path = "/user";
        RegisterResponse resp =  this.makeRequest("POST", path, user, RegisterResponse.class, null);
        this.authToken = resp.authToken();
        return resp;
    }

    public LoginResponse loginUser(LoginRequest login) throws ResponseException {
        var path = "/session";
        LoginResponse resp =  this.makeRequest("POST", path, login, LoginResponse.class, null);
        this.authToken = resp.authToken();
        return resp;
    }

    public void logoutUser() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, this.authToken);
    }

    public Collection<GameModel> listGames() throws ResponseException {
        var path = "/game";
        ListGameResponse resp = this.makeRequest("GET", path, null, ListGameResponse.class, this.authToken);
        return resp.games();
    }

    public int createGame(String gameName) throws ResponseException {
        var path = "/game";
        CreateGameData gameRec = new CreateGameData(gameName);
        CreateGameResponse resp =  this.makeRequest("POST", path, gameRec, CreateGameResponse.class, authToken);
        return resp.gameID();
    }

    public void joinGame(JoinGameData data) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, data, null, authToken);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String auth) throws ResponseException {
        try {
            URL url = (new URI(serverURL + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http, auth);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http, String auth) throws IOException {
        if (auth != null) {
            http.addRequestProperty("authorization", auth);
        }
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status/100 == 2;
    }
}

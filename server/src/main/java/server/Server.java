package server;

import dataAccess.SQLDAO;
import handler.*;
import server.websocket.WebsocketHandler;
import service.ClearService;
import spark.*;

public class Server {
    private final WebsocketHandler websocketHandler;

    public Server() {
        websocketHandler = new WebsocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", websocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) -> new ClearHandler().clear(req, res));
        Spark.delete("/session", (req, res) -> new LogoutHandler().logout(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler().login(req, res));
        Spark.post("/user", (req, res) -> new RegisterHandler().register(req, res));
        Spark.get("/game", (req, res) -> new GameHandler().listGames(req, res));
        Spark.post("/game", (req, res) -> new GameHandler().createGame(req, res));
        Spark.put("/game", (req, res) -> new GameHandler().joinGame(req, res));
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public int port() {
        return Spark.port();
    }
}

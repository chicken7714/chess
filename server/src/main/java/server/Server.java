package server;

import handler.ClearHandler;
import handler.LoginHandler;
import handler.LogoutHandler;
import handler.RegisterHandler;
import service.ClearService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req, res) -> new ClearHandler().clear(req, res));
        Spark.delete("/session", (req, res) -> new LogoutHandler().logout(req, res));
        Spark.post("/session", (req, res) -> new LoginHandler().login(req, res));
        Spark.post("/user", (req, res) -> new RegisterHandler().register(req, res));
        // Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

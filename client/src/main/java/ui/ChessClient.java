package ui;

import ServerFacade.ServerFacade;
import ServerFacade.ResponseException;
import model.UserModel;
import request.LoginRequest;

import java.util.Arrays;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.PRELOGIN;
    private String username;


    public ChessClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;

    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> quit();
                case "login" -> login(params);
                case "register" -> register(params);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String quit() {
        return "quit";
    }

    private String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            this.username = params[0];
            server.loginUser(new LoginRequest(params[0], params[1]));
            state = State.POSTLOGIN;
            return String.format("You signed in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    private String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            this.username = params[0];
            server.registerUser(new UserModel(params[0], params[1], params[2]));
            state = State.POSTLOGIN;
            return String.format("You were registered and signed in as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }


    public String help() {
        if (state.equals(State.PRELOGIN)) {
            return """
                   -register <username> <password> <email>
                   -login <username> <password>
                   -quit
                   -help
                   """;
        }
        else if (state.equals(State.POSTLOGIN)) {
            return """
                   -create <NAME>
                   -list
                   -join <ID> [WHITE|BLACK|<empty>]
                   -observe <ID>
                   -logout
                   -quit
                   -help
                   """;
        }
        return "";
    }
}
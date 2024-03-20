package ui;

import ServerFacade.ServerFacade;
import ServerFacade.ResponseException;
import model.GameModel;
import model.UserModel;
import request.LoginRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.PRELOGIN;
    private String username;
    private ArrayList<GameModel> gameArray;


    public ChessClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;

    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state.equals(State.PRELOGIN)) {
                return switch (cmd) {
                    case "quit" -> quit();
                    case "login" -> login(params);
                    case "register" -> register(params);
                    default -> help();
                };
            } else if (state.equals(State.POSTLOGIN)) {
                return switch (cmd) {
                    case "quit" -> quit();
                    case "creategame" -> createGame(params);
                    case "listgames" -> listGames();
                    case "joingame" -> joinGame(params);
                    case "observegame" -> observeGame(params);
                    case "logout" -> logout();
                    default -> help();
                };
            }
            else if (state.equals(State.GAMEPLAY)) {
                return switch (cmd) {
                    case "quit" -> quit();
                    default -> help();
                };
            }
            return "";
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

    private String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            int gameID = server.createGame(params[0]);
            return String.format("Created Game %s with gameID %d", params[0], gameID);
        }
        throw new ResponseException(400, "Expected: <gameName>");
    }

    private String listGames() throws ResponseException {
        int index = 1;
        Collection<GameModel> games = server.listGames();
        StringBuilder stringBuilder = new StringBuilder();
        for (GameModel game : games) {
            String gameString = String.format("%d: Game Name - %s, White Player - %s, Black Player - %s",
                    index, game.gameName(), game.whiteUsername(), game.blackUsername());
            stringBuilder.append(gameString);
            stringBuilder.append("\n");
            this.gameArray.add(index, game);
            index += 1;
        }
        return stringBuilder.toString();
    }

    private String joinGame(String... params) {
        state = State.GAMEPLAY;
        return "Joined gameID";
    }

    private String observeGame(String... params) {
        return "Observing Game";
    }

    private String logout() throws ResponseException {
        server.logoutUser();
        state = State.PRELOGIN;
        return "Logged out user";
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
                   -createGame <NAME>
                   -listGames
                   -joinGame <ID> [WHITE|BLACK|<empty>]
                   -observeGame <ID>
                   -logout
                   -quit
                   -help
                   """;
        }
        return "";
    }
}

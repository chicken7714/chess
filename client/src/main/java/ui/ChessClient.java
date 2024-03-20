package ui;

import ServerFacade.ServerFacade;
import ServerFacade.ResponseException;
import model.GameModel;
import model.UserModel;
import request.JoinGameData;
import request.LoginRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.PRELOGIN;
    private String username;
    private HashMap<Integer, GameModel> gameMap;


    public ChessClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.gameMap = new HashMap<Integer, GameModel>();
    }

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
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
                    case "createGame" -> createGame(params);
                    case "listGames" -> listGames();
                    case "joinGame" -> joinGame(params);
                    case "observeGame" -> observeGame(params);
                    case "logout" -> logout();
                    default -> help();
                };
            }
            else if (state.equals(State.GAMEPLAY)) {
                return switch (cmd) {
                    case "quit" -> quit();
                    case "back" -> back();
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
        for (var game : games) {
            String gameString = String.format("%d: Game Name - %s, White Player - %s, Black Player - %s",
                    index, game.gameName(), game.whiteUsername(), game.blackUsername());
            stringBuilder.append(gameString);
            stringBuilder.append("\n");
            gameMap.put(index, game);
            index += 1;
        }
        System.out.println(gameMap.toString());
        return stringBuilder.toString();
    }

    private String joinGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            try {
                GameModel targetGame = gameMap.get(Integer.parseInt(params[0]));
                System.out.println(params[1]);
                System.out.println(targetGame.gameID());
                JoinGameData joinGameData = new JoinGameData(params[1], targetGame.gameID());
                server.joinGame(joinGameData);
                state = State.GAMEPLAY;
                return "Successfully Joined Game";
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Expected <ID> [WHITE|BLACK|<empty>]");
            }
        }
        throw new ResponseException(400, "Expected <ID> [WHITE|BLACK|<empty>]");
    }

    private String observeGame(String... params) {
        return "Observing Game";
    }

    private String logout() throws ResponseException {
        server.logoutUser();
        state = State.PRELOGIN;
        return "Logged out user";
    }

    private String back() {
        state = State.POSTLOGIN;
        return "Returning to Postlogin UI";
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

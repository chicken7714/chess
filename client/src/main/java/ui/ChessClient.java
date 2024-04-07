package ui;

import ServerFacade.ServerFacade;
import ServerFacade.ResponseException;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameModel;
import model.UserModel;
import request.JoinGameData;
import request.LoginRequest;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.ServerMessageHandler;
import websocket.WebsocketFacade;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class ChessClient implements ServerMessageHandler {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.PRELOGIN;
    private String username;
    private String authToken;
    private HashMap<Integer, GameModel> gameMap;
    private ChessBoardDrawer chessBoardDrawer;
    private WebsocketFacade ws;
    private ChessGame game;
    private int gameID;


    public ChessClient(String serverUrl) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.gameMap = new HashMap<Integer, GameModel>();
        this.chessBoardDrawer = new ChessBoardDrawer();
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
                    case "redraw" -> redraw();
                    case "leave" -> leave();
                    case "makeMove" -> makeMove(params);
                    case "resign" -> resign();
                    case "highlight" -> highlight(params);
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
        return stringBuilder.toString();
    }

    private String joinGame(String... params) throws ResponseException {
        System.out.println("JOIN GAME CHESS CLIENT");
        if (params.length >= 1) {
            try {
                GameModel targetGame = gameMap.get(Integer.parseInt(params[0]));
                JoinGameData joinGameData = new JoinGameData(params[1], targetGame.gameID());
                String authToken = server.joinGame(joinGameData);
                this.authToken = authToken;
                this.gameID = joinGameData.gameID();

                ChessGame.TeamColor teamColor = null;
                if (params[1].equals("WHITE")) {
                    teamColor = ChessGame.TeamColor.WHITE;
                } else if (params[1].equals("BLACK")) {
                    teamColor = ChessGame.TeamColor.BLACK;
                }
                ws.joinPlayer(authToken,targetGame.gameID(), teamColor);
                state = State.GAMEPLAY;
                return "Successfully Joined Game";
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Expected <ID> [WHITE|BLACK|<empty>]");
            }
        }
        throw new ResponseException(400, "Expected <ID> [WHITE|BLACK|<empty>]");
    }

    private String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            try {
                GameModel targetGame = gameMap.get(Integer.parseInt(params[0]));
                JoinGameData joinGameData = new JoinGameData(null, targetGame.gameID());
                String authToken = server.joinGame(joinGameData);
                ws.joinObserver(authToken, targetGame.gameID());
                state = State.GAMEPLAY;
                return String.format("Successfully observing game %s", params[0]);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Expected <ID>");
            }
        }
        throw new ResponseException(400, "Expected <ID>");
    }

    private String logout() throws ResponseException {
        server.logoutUser();
        state = State.PRELOGIN;
        return "Logged out user";
    }

    private String redraw() {
        return chessBoardDrawer.generateChessBoard(this.game);
    }

    private String leave() throws ResponseException {
        ws.leaveGame(this.authToken, this.gameID);
        return "You have left the game";
    }

    private String makeMove(String... params) {
        return "";
    }

    private String resign() throws ResponseException {
        ws.resignGame(this.authToken, this.gameID);
        return "You have resigned from the game";
    }

    private String highlight(String... params) {
        return "";
    }

    public String help() {
        if (state.equals(State.PRELOGIN)) {
            return """
                   -register <username> <password> <email> (NEW USERS)
                   -login <username> <password> (EXISTING USERS)
                   -quit (terminates the application)
                   -help
                   """;
        } else if (state.equals(State.POSTLOGIN)) {
            return """
                   -createGame <NAME> (creates a new game of chess)
                   -listGames (list all the games that have been created, must list games before joining/observing)
                   -joinGame <ID> [WHITE|BLACK|<empty>] (join a game of chess with the given ID)
                   -observeGame <ID> (observe a game of chess with the given ID)
                   -logout (logs you out of the application)
                   -quit (terminates the application, does not log you out!)
                   -help
                   """;
        } else if (state.equals(State.GAMEPLAY)) {
            return """
                   -redraw (redraws chess board)
                   -leave (removes player from game)
                   -makeMove [startPos] [endPos] (makes the given move on the chess board)
                   -resign (you forfeit current game and it becomes unplayable)
                   -highlight [startPos] (highlights all possible moves with piece given by startPos)
                   -help
                   """;
        }
        return "";
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        switch (serverMessage.getServerMessageType()) {
            case ERROR -> error((ErrorMessage) serverMessage);
            case LOAD_GAME -> loadGame((LoadGameMessage) serverMessage);
            case NOTIFICATION -> notification((NotificationMessage) serverMessage);
        }
    }

    @Override
    public void updateGame(ChessGame game) {
        this.game = game;
    }

    private void error(ErrorMessage message) {
        System.out.println(message.getErrorMessage());
    }

    private void loadGame(LoadGameMessage loadGame) {
        GameModel game = new Gson().fromJson(loadGame.getGame(), GameModel.class);
        updateGame(game.game());
        System.out.println(chessBoardDrawer.generateChessBoard(game.game()));
    }

    private void notification(NotificationMessage notificationMessage) {
        System.out.println(notificationMessage.getMessage());
    }
}

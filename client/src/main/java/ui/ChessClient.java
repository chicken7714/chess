package ui;

import ServerFacade.ServerFacade;
import ServerFacade.ResponseException;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import javax.websocket.OnMessage;
import java.util.*;

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
    private ChessGame.TeamColor teamColor = null;
    private Scanner scanner;


    public ChessClient(String serverUrl, Scanner scanner) {
        this.server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.gameMap = new HashMap<Integer, GameModel>();
        this.chessBoardDrawer = new ChessBoardDrawer();
        this.scanner = scanner;
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
        if (params.length >= 1) {
            try {
                GameModel targetGame = gameMap.get(Integer.parseInt(params[0]));
                JoinGameData joinGameData = new JoinGameData(params[1], targetGame.gameID());
                String authToken = server.joinGame(joinGameData);
                this.authToken = authToken;

                ChessGame.TeamColor teamColor = null;
                if (params[1].equals("WHITE")) {
                    teamColor = ChessGame.TeamColor.WHITE;
                } else if (params[1].equals("BLACK")) {
                    teamColor = ChessGame.TeamColor.BLACK;
                }

                ws = new WebsocketFacade(serverUrl, this);
                this.teamColor = teamColor;
                this.gameID = joinGameData.gameID();
                ws.joinPlayer(authToken,targetGame.gameID(), teamColor);
                state = State.GAMEPLAY;
                return "Successfully Joined Game";
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Expected <ID> [WHITE|BLACK]");
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
                this.authToken = authToken;
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
        return chessBoardDrawer.generateChessBoard(this.game, this.teamColor);
    }

    private String leave() throws ResponseException {
        ws.leaveGame(this.authToken, this.gameID);
        state = State.POSTLOGIN;
        return "You have left the game";
    }

    private String makeMove(String... params) throws ResponseException {
        if (params.length >= 2) {
            if (params[0].length() > 2 || params[1].length() > 2) {
                throw new ResponseException(400, "Start and end positions only can be 2 characters long");
            }
            if (!params[0].matches("^[a-h][1-8]$") || !params[1].matches("^[a-h][1-8]$")) {
                throw new ResponseException(400, "Invalid move input");
            }

            String endPos = params[1].substring(1);

            Map<String, Integer> rowColMap = new HashMap<>();
            rowColMap.put("a", 1);
            rowColMap.put("b", 2);
            rowColMap.put("c", 3);
            rowColMap.put("d", 4);
            rowColMap.put("e", 5);
            rowColMap.put("f", 6);
            rowColMap.put("g", 7);
            rowColMap.put("h", 8);

            int startPosCol = rowColMap.get(params[0].substring(0, 1).toLowerCase());
            int startPosRow = Integer.parseInt(params[0].substring(1));
            int endPosCol = rowColMap.get(params[1].substring(0, 1).toLowerCase());
            int endPosRow = Integer.parseInt(params[1].substring(1));
            ChessMove move = new ChessMove(new ChessPosition(startPosRow, startPosCol), new ChessPosition(endPosRow, endPosCol));
            ws.makeMove(this.authToken, this.gameID, move);
        } else {
            throw new ResponseException(400, "Expected: makeMove <startPos> <endPos>");
        }
        return "";
    }

    private String resign() throws ResponseException {
        System.out.println("Are you sure you want to resign? (yes/no)");
        System.out.print(">>> ");
        String line = scanner.nextLine();

        if (line.equals("yes")) {
            ws.resignGame(this.authToken, this.gameID);
            return "You have resigned from the game";
        } else {
            return "Cancelling resignation";
        }

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
                   -joinGame <ID> [WHITE|BLACK] (join a game of chess with the given ID)
                   -observeGame <ID> (observe a game of chess with the given ID)
                   -logout (logs you out of the application)
                   -quit (terminates the application, does not log you out!)
                   -help
                   """;
        } else if (state.equals(State.GAMEPLAY)) {
            return """
                   -redraw (redraws chess board)
                   -leave (removes player from game)
                   -makeMove <startPos> <endPos> (makes the given move on the chess board)
                   -resign (you forfeit current game and it becomes unplayable)
                   -highlight <startPos> (highlights all possible moves with piece given by startPos)
                   -help
                   """;
        }
        return "";
    }

    @Override
    @OnMessage
    public void notify(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);

        switch (serverMessage.getServerMessageType()) {
            case ERROR -> error(message);
            case LOAD_GAME -> loadGame(message);
            case NOTIFICATION -> notification(message);
        }
    }

    @Override
    public void updateGame(ChessGame game) {
        this.game = game;
    }

    private void error(String message) {
        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
        System.out.println(errorMessage.getErrorMessage());
    }

    private void loadGame(String message) {
        System.out.println(message);
        LoadGameMessage loadGame = new Gson().fromJson(message, LoadGameMessage.class);
        GameModel game = new Gson().fromJson(loadGame.getGame(), GameModel.class);

        updateGame(game.game());
        System.out.println(chessBoardDrawer.generateChessBoard(game.game(), teamColor));
    }

    private void notification(String message) {
        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);

        System.out.println(notificationMessage.getMessage());
    }
}

package dataAccess;

import model.GameModel;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private static HashMap<Integer, GameModel> games = new HashMap<>();
    private static int idCount = 1;
    @Override
    public int createGame(String gameName) {
        int gameID = idCount;
        idCount++;

        GameModel newGame = new GameModel(gameID, null, null, gameName);
        games.put(gameID, newGame);
        return gameID;
    }

    @Override
    public Collection<GameModel> listGames() {
        return games.values();
    }

    @Override
    public void addUser(int gameID, String username, String playerColor) throws DataAccessException {
        GameModel game = games.get(gameID);
        String gameName = game.gameName();
        String blackUsername = game.blackUsername();
        String whiteUsername = game.whiteUsername();

        if (playerColor.equals("WHITE") && (whiteUsername == null)) {
            whiteUsername = username;
            GameModel newGame = new GameModel(gameID, whiteUsername, blackUsername, gameName);
            games.put(gameID, newGame);
        } else if (playerColor.equals("BLACK") && (blackUsername == null)) {
            blackUsername = username;
            GameModel newGame = new GameModel(gameID, whiteUsername, blackUsername, gameName);
            games.put(gameID, newGame);
        } else {
            throw new DataAccessException("Username already taken");
        }
    }

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public void isValidGame(int gameID) throws DataAccessException {
        GameModel game = games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Bad gameID");
        }
    }
}

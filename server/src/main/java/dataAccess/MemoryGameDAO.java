package dataAccess;

import model.GameModel;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private static HashMap<Integer, GameModel> games = new HashMap<>();
    @Override
    public void createGame(int gameID, GameModel game) {
        games.put(gameID, game);
    }

    @Override
    public GameModel getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public Collection<GameModel> listGames() {
        return games.values();
    }

    @Override
    public void updateGame(GameModel updatedGame, int gameID) throws DataAccessException {
        games.put(gameID, updatedGame);
    }

    @Override
    public void clear() {
        games.clear();
    }
}

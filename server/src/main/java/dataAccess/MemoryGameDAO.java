package dataAccess;

import model.GameModel;

import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO {
    private static HashSet<GameModel> games = new HashSet<>();
    @Override
    public void createGame(GameModel game) {
        games.add(game);
    }

    @Override
    public GameModel getGame(int gameID) throws DataAccessException {
        for (GameModel game: games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Game not found!");
    }

    @Override
    public Collection<GameModel> listGames() {
        return games;
    }

    @Override
    public void updateGame(GameModel updatedGame, int gameID) throws DataAccessException {
        boolean isUpdated = false;
        for (GameModel game : games) {
            if (game.gameID() == gameID) {
                game = updatedGame;
                isUpdated = true;
            }
        }
        if (!isUpdated) {
            throw new DataAccessException("Game not found");
        }
    }

    @Override
    public void clear() {
        games.clear();
    }
}

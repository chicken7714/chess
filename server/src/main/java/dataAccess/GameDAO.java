package dataAccess;

import chess.ChessGame;
import model.GameModel;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.HashSet;

public interface GameDAO {
    void createGame(GameModel game);
    GameModel getGame(int gameID) throws DataAccessException;
    Collection<GameModel> listGames();
    void updateGame(GameModel updatedGame, int gameID) throws DataAccessException;
    void clear();
}

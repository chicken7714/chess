package dataAccess;

import chess.ChessGame;
import model.GameModel;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.HashSet;

public interface GameDAO {
    int createGame(String gameName);
    GameModel getGame(int gameID) throws DataAccessException;
    Collection<GameModel> listGames();
    void addUser(int gameID, String username, String playerColor) throws DataAccessException;

    void clear();
}

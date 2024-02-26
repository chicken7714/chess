package dataAccess;

import chess.ChessGame;
import model.GameModel;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.HashSet;

public interface GameDAO {
    int createGame(String gameName);
    Collection<GameModel> listGames();
    void addUser(int gameID, String username, String playerColor) throws DataAccessException;
    void clear();
    void isValidGame(int gameID) throws DataAccessException;
}

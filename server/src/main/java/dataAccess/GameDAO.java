package dataAccess;

import model.GameModel;

import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName) throws DataAccessException;
    Collection<GameModel> listGames() throws DataAccessException;
    void addUser(int gameID, String username, String playerColor) throws DataAccessException;
    void clear() throws DataAccessException;
    void isValidGame(int gameID) throws DataAccessException;
}

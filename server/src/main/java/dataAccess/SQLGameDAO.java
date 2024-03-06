package dataAccess;

import model.GameModel;

import java.util.Collection;

public class SQLGameDAO extends SQLDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        super();
    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public Collection<GameModel> listGames() {
        return null;
    }

    @Override
    public void addUser(int gameID, String username, String playerColor) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    @Override
    public void isValidGame(int gameID) throws DataAccessException {

    }
}

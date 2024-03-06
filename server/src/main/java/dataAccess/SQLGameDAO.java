package dataAccess;

import com.google.gson.Gson;
import model.GameModel;

import java.util.Collection;

public class SQLGameDAO extends SQLDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        super();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game DEFAULT VALUES";
        var gameID = executeUpdate(statement);

        GameModel game = new GameModel(gameID, null, null, gameName);
        var serializer = new Gson();
        String json = serializer.toJson(game);
        statement = "UPDATE game SET json=? WHERE gameID=?";
        executeUpdate(statement, json, gameID);

        return gameID;
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

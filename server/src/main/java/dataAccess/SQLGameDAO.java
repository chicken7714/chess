package dataAccess;

import com.google.gson.Gson;
import model.GameModel;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class SQLGameDAO extends SQLDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        super();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        System.out.println("Creating game");
        var statement = "INSERT INTO game (json) VALUES ('')";
        var gameID = executeUpdate(statement);
        System.out.println("Created game, id = ");
        System.out.println(gameID);

        GameModel game = new GameModel(gameID, null, null, gameName);
        var serializer = new Gson();
        String json = serializer.toJson(game);
        statement = "UPDATE game SET json=? WHERE gameID=?";
        executeUpdate(statement, json, gameID);

        return gameID;
    }

    @Override
    public Collection<GameModel> listGames() throws DataAccessException {
        HashSet<GameModel> games = new HashSet<>();
        Gson serializer = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var json = rs.getString(1);
                        GameModel game = serializer.fromJson(json, GameModel.class);
                        games.add(game);
                    }
                    return games;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addUser(int gameID, String username, String playerColor) throws DataAccessException {
        var serializer = new Gson();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var json = rs.getString(1);
                        GameModel game = serializer.fromJson(json, GameModel.class);
                        if (playerColor.equals("WHITE") && (game.whiteUsername() == null)) {
                            GameModel newGame = new GameModel(gameID, username, game.blackUsername(), game.gameName());
                            var newJson = serializer.toJson(newGame);
                            statement = "UPDATE game SET json=? WHERE gameID=?";
                            executeUpdate(statement, newJson, gameID);
                        } else if (playerColor.equals("BLACK") && (game.blackUsername() == null)) {
                            GameModel newGame = new GameModel(gameID, username, game.whiteUsername(), game.gameName());
                            var newJson = serializer.toJson(newGame);
                            statement = "UPDATE game SET json=? WHERE gameID=?";
                            executeUpdate(statement, newJson, gameID);
                        } else {
                            throw new DataAccessException("Error: User already taken");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    @Override
    public void isValidGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new DataAccessException("Not Valid Request, no game found");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}

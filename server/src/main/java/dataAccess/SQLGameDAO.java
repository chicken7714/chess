package dataAccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GameModel;
import chess.ChessGame;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class SQLGameDAO extends SQLDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        super();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (json) VALUES ('')";
        var gameID = executeUpdate(statement);

        GameModel game = new GameModel(gameID, null, null, gameName, new ChessGame());
        var serializer = new GsonBuilder().serializeNulls().create();
        String json = serializer.toJson(game);
        statement = "UPDATE game SET json=? WHERE gameID=?";
        executeUpdate(statement, json, gameID);

        return gameID;
    }

    @Override
    public Collection<GameModel> listGames() throws DataAccessException {
        ArrayList<GameModel> games = new ArrayList<>();
        Gson serializer = new GsonBuilder().serializeNulls().create();
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
        var serializer = new GsonBuilder().serializeNulls().create();

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var json = rs.getString(1);
                        GameModel game = serializer.fromJson(json, GameModel.class);
                        if (playerColor.equals("WHITE") && (game.whiteUsername() == null)) {
                            GameModel newGame = new GameModel(gameID, username, game.blackUsername(), game.gameName(), game.game());
                            var newJson = serializer.toJson(newGame);
                            statement = "UPDATE game SET json=? WHERE gameID=?";
                            executeUpdate(statement, newJson, gameID);
                        } else if (playerColor.equals("BLACK") && (game.blackUsername() == null)) {
                            GameModel newGame = new GameModel(gameID, game.whiteUsername(), username, game.gameName(), game.game());
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

    public String getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("json");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        throw new DataAccessException("Not a valid request, no game found");
    }

    public void updateGame(int gameID, String gameJson) throws DataAccessException {
        var statement = "UPDATE game SET json=? WHERE gameID=?";
        executeUpdate(statement, gameJson, gameID);
    }
}

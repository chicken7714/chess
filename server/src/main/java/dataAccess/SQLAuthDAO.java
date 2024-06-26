package dataAccess;

import model.AuthModel;
import service.UnauthorizedAccessException;

import java.sql.SQLException;

public class SQLAuthDAO extends SQLDAO implements AuthDAO {

    public SQLAuthDAO() throws DataAccessException {
        super();
    }
    @Override
    public void createAuth(AuthModel newAuth) throws DataAccessException {
        var statement = "INSERT INTO auth (auth, username) VALUES (?, ?)";
        executeUpdate(statement, newAuth.authToken(), newAuth.username());
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException, UnauthorizedAccessException {
        if (checkValidAuth(authToken) != null) {
            var statement = "DELETE FROM auth WHERE auth=?";
            executeUpdate(statement, authToken);
        } else {
            throw new UnauthorizedAccessException("Error: Invalid Auth token");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        executeUpdate(statement);
    }

    @Override
    public String checkValidAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM auth WHERE auth=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;

    }
}

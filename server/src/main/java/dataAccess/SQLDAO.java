package dataAccess;

import java.sql.SQLException;

public class SQLDAO {

    private static String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL
            PRIMARY KEY (`username`)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
            `auth` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL
            PRIMARY KEY (`auth`)
            FOREIGN KEY(username) REFERENCES user(username)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS game (
            `gameID` int NOT NULL AUTO_INCREMENT,
            `json` TEXT DEFAULT NULL,
            PRIMARY KEY (`gameID`))
            """
    };
    public SQLDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement: createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}

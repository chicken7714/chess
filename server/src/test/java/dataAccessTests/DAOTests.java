package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.AuthModel;
import model.GameModel;
import model.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.UnauthorizedAccessException;

import java.util.Collection;

public class DAOTests {

    @BeforeEach
    public void beforeEach() throws DataAccessException {
        var userdao = new SQLUserDAO();
        var authdao = new SQLAuthDAO();
        var gamedao = new SQLGameDAO();
        userdao.clear();
        authdao.clear();
        gamedao.clear();
    }

    @Test
    @Order(1)
    public void positiveCreateUser() throws DataAccessException {
        var userdao = new SQLUserDAO();
        UserModel user = new UserModel("Hello", "World", "gmail@gmail.com");
        userdao.createUser(user);
        Assertions.assertTrue(userdao.isInDatabase("Hello"));
    }

    @Test
    @Order(2)
    public void negativeCreateUser() throws DataAccessException {
        var userdao = new SQLUserDAO();
        UserModel user = new UserModel("Hello", "World", "gmail@gmail.com");
        userdao.createUser(user);
        var user2 = new UserModel("Hello", "World", "gmail@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> {userdao.createUser(user2);});
    }

    @Test
    @Order(3)
    public void positiveIsValiduser() throws DataAccessException {
        var userdao = new SQLUserDAO();
        UserModel user = new UserModel("Hello", "World", "gmail.com");
        userdao.createUser(user);
        Assertions.assertTrue(userdao.isValidUser("Hello", "World"));
    }

    @Test
    @Order(4)
    public void negativeIsValidUser() throws DataAccessException {
        var userdao = new SQLUserDAO();
        UserModel user = new UserModel("Hello", "World", "gmail.com");
        userdao.createUser(user);
        Assertions.assertFalse(userdao.isValidUser("Hello", "Worl"));
    }

    @Test
    @Order(5)
    public void positiveIsInDatabase() throws DataAccessException {
        var userdao = new SQLUserDAO();
        UserModel user = new UserModel("Hello", "My", "Name.com");
        userdao.createUser(user);
        Assertions.assertTrue(userdao.isInDatabase("Hello"));
    }

    @Test
    @Order(6)
    public void negativeIsInDatabase() throws DataAccessException {
        var userdao = new SQLUserDAO();
        UserModel user = new UserModel("Hello", "My", "Name.com");
        userdao.createUser(user);
        Assertions.assertFalse(userdao.isInDatabase("Helo"));
    }

    @Test
    @Order(7)
    public void userClear() throws DataAccessException {
        var userdao = new SQLUserDAO();
        UserModel user = new UserModel("Hello", "My", "Name.com");
        userdao.createUser(user);
        userdao.clear();
        Assertions.assertFalse(userdao.isInDatabase("Hello"));
    }

    @Test
    @Order(8)
    public void positiveCreateAuth() throws DataAccessException {
        var authdao = new SQLAuthDAO();
        AuthModel auth = new AuthModel("Thisismyauth", "Henry");
        authdao.createAuth(auth);
        Assertions.assertEquals("Henry", authdao.checkValidAuth("Thisismyauth"));
    }

    @Test
    @Order(9)
    public void negativeCreateAuth() throws DataAccessException {
        var authdao = new SQLAuthDAO();
        AuthModel auth = new AuthModel("AuthToken", "username");
        authdao.createAuth(auth);
        AuthModel auth2 = new AuthModel("AuthToken", "username2");
        Assertions.assertThrows(DataAccessException.class, () -> {authdao.createAuth(auth2);});
    }

    @Test
    @Order(10)
    public void positiveDeleteAuth() throws DataAccessException, UnauthorizedAccessException {
        var authdao = new SQLAuthDAO();
        AuthModel auth = new AuthModel("AuthToken", "username");
        authdao.createAuth(auth);
        authdao.deleteAuth(auth.authToken());
        Assertions.assertNull(authdao.checkValidAuth(auth.authToken()));
    }

    @Test
    @Order(11)
    public void negativeDeleteAuth() throws DataAccessException, UnauthorizedAccessException {
        var authdao = new SQLAuthDAO();
        AuthModel auth = new AuthModel("AuthToken", "username");
        authdao.createAuth(auth);
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> {authdao.deleteAuth("AthuTkeon");});
    }

    @Test
    @Order(12)
    public void positiveCheckValidAuth() throws DataAccessException {
        var authdao = new SQLAuthDAO();
        AuthModel auth = new AuthModel("AuthToken", "username");
        authdao.createAuth(auth);
        Assertions.assertEquals("username", authdao.checkValidAuth("AuthToken"));
    }

    @Test
    @Order(13)
    public void negativeCheckValidAuth() throws DataAccessException {
        var authdao = new SQLAuthDAO();
        AuthModel auth = new AuthModel("AuthToken", "username");
        authdao.createAuth(auth);
        Assertions.assertNull(authdao.checkValidAuth("AuthTken"));
    }

    @Test
    @Order(14)
    public void authClear() throws DataAccessException {
        var authdao = new SQLAuthDAO();
        AuthModel auth = new AuthModel("AuthToken", "username");
        authdao.createAuth(auth);
        authdao.clear();
        Assertions.assertNull(authdao.checkValidAuth("AuthToken"));
    }

    @Test
    @Order(15)
    public void positiveCreateGame() throws DataAccessException {
        var gamedao = new SQLGameDAO();
        var myInt = gamedao.createGame("Hello");
        Assertions.assertEquals(1, myInt);
    }

    @Test
    @Order(16)
    public void negativeCreateGame() throws DataAccessException {
        var gamedao = new SQLGameDAO();
        var myInt = gamedao.createGame("");
        Assertions.assertEquals(1, myInt);
    }

    @Test
    @Order(17)
    public void positiveListGames() throws DataAccessException {
        var gamedao = new SQLGameDAO();
        gamedao.createGame("Badboiz");
        gamedao.createGame("HelloWorld");
        gamedao.createGame("Dijkstra's pawns");
        System.out.println(gamedao.listGames().toString());
        Assertions.assertEquals(3, gamedao.listGames().size());
    }

    @Test
    @Order(18)
    public void negativeListGames() throws DataAccessException {
        var gamedao = new SQLGameDAO();
        gamedao.createGame("Badboiz");
        gamedao.createGame("HelloWorld");
        gamedao.createGame("Dijkstra's pawns");
        gamedao.clear();
        Assertions.assertEquals(0, gamedao.listGames().size());
    }

    @Test
    @Order(19)
    public void positiveAddUser() throws DataAccessException {
        var gamedao = new SQLGameDAO();
        var gameInt = gamedao.createGame("Badboiz");
        gamedao.addUser(gameInt, "Wah-D", "WHITE");
        Collection<GameModel> games = gamedao.listGames();
        Assertions.assertEquals(games.size(), 1);
    }

    @Test
    @Order(20)
    public void negativeAddUser() throws DataAccessException {
        var gamedao = new SQLGameDAO();
        var gameInt = gamedao.createGame("Badboiz");
        gamedao.addUser(gameInt, "Wah-D", "WHITE");
        Assertions.assertThrows(DataAccessException.class, () -> {gamedao.addUser(gameInt, "Wah-D", "WHITE");});
    }

    @Test
    @Order(21)
    public void positiveIsValidGame() throws DataAccessException {
        var gamedao = new SQLGameDAO();
        var gameInt = gamedao.createGame("Badboiz");
        Assertions.assertDoesNotThrow(() -> gamedao.isValidGame(gameInt));
    }

    @Test
    @Order(22)
    public void negativeIsValidGame() throws DataAccessException {
        var gamedao = new SQLGameDAO();
        var gameInt = gamedao.createGame("Badboiz");
        Assertions.assertThrows(DataAccessException.class, () -> gamedao.isValidGame(gameInt+1));
    }

    @Test
    @Order(23)
    public void clearGame() throws DataAccessException {
        var gamedao = new SQLGameDAO();
        gamedao.createGame("Badboiz");
        gamedao.createGame("HelloWorld");
        gamedao.createGame("Dijkstra's pawns");
        gamedao.clear();
        Assertions.assertEquals(0, gamedao.listGames().size());
    }

}

package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.UserModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

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

}

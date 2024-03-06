package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class DAOTests {

    @BeforeEach
    public void beforeEach() {
    }

    @Test
    @Order(1)
    public void positiveCreateUser() throws DataAccessException {
        var userdao = new SQLUserDAO();
        UserModel user = new UserModel("Hello", "World", "gmail@gmail.com");
        userdao.createUser(user);
    }

}

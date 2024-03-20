package clientTests;

import ServerFacade.ServerFacade;
import ServerFacade.ResponseException;
import model.GameModel;
import model.UserModel;
import org.junit.jupiter.api.*;
import request.*;
import response.CreateGameResponse;
import response.ListGameResponse;
import response.LoginResponse;
import response.RegisterResponse;
import server.Server;

import java.util.Collection;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + server.port();
        facade = new ServerFacade(url);
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void before() throws ResponseException {
        facade.clear();
    }


    @Test
    @Order(1)
    public void registerPos() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);
        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);
        Assertions.assertEquals("Hello", resp.username());
    }

    @Test
    @Order(7)
    public void registerNeg() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);
        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        facade.registerUser(user);
        Assertions.assertThrows(ResponseException.class, () -> facade.registerUser(user));
    }

    @Test
    @Order(2)
    public void loginPos() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);
        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);
        Assertions.assertEquals("Hello", resp.username());

        LoginRequest request = new LoginRequest("Hello", "password");
        LoginResponse loginResp = facade.loginUser(request);
        String auth = loginResp.authToken();
        Assertions.assertInstanceOf(String.class, auth);
    }

    @Test
    @Order(8)
    public void loginNeg() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        LoginRequest request = new LoginRequest("Hello", "password");
        try {
            facade.loginUser(request);
        } catch (ResponseException e) {
            Assertions.assertEquals(500, e.statusCode());
        }
    }

    @Test
    @Order(3)
    public void logoutPos() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);
        Assertions.assertEquals("Hello", resp.username());

        LoginRequest request = new LoginRequest("Hello", "password");
        LoginResponse loginResp = facade.loginUser(request);
        String auth = loginResp.authToken();
        Assertions.assertDoesNotThrow(facade::logoutUser);
    }

    @Test
    @Order(9)
    public void logoutNeg() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        try {
            facade.logoutUser();
        } catch (ResponseException e) {
            Assertions.assertEquals(500, e.statusCode());
        }
    }

    @Test
    @Order(4)
    public void createGamePos() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);

        int gameResp = facade.createGame("Chess4Nerds");
        Assertions.assertInstanceOf(Integer.class, gameResp);
    }

    @Test
    @Order(10)
    public void createGameNeg() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        Assertions.assertThrows(ResponseException.class, () -> facade.createGame("Hello"));

    }

    @Test
    @Order(5)
    public void listGamePos() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);

        int gameResp = facade.createGame("Chess4Nerds");
        System.out.println(gameResp);

        Collection<GameModel> listGame = facade.listGames();
        Assertions.assertEquals(1, listGame.size());
    }

    @Test
    @Order(11)
    public void listGameNeg() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        Assertions.assertThrows(ResponseException.class, () -> facade.listGames());
    }

    @Test
    @Order(6)
    public void joinGamePos() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);
        String auth = resp.authToken();

        int gameResp = facade.createGame("Chess4Nerds");
        Assertions.assertInstanceOf(Integer.class, gameResp);

        Assertions.assertDoesNotThrow(() -> facade.joinGame(new JoinGameData("WHITE", gameResp)));
        Collection<GameModel> listResp = facade.listGames();
        System.out.println(listResp.toString());
    }

    @Test
    @Order(12)
    public void joinGameNeg() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);
        String auth = resp.authToken();

        Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(new JoinGameData("WHITE", 1)));
    }

}

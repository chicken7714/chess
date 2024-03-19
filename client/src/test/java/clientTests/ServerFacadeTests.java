package clientTests;

import ServerFacade.ServerFacade;
import ServerFacade.ResponseException;
import model.UserModel;
import org.junit.jupiter.api.*;
import request.*;
import response.CreateGameResponse;
import response.ListGameResponse;
import response.LoginResponse;
import response.RegisterResponse;
import server.Server;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws ResponseException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
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
    @Order(4)
    public void createGamePos() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);
        String auth = resp.authToken();

        CreateGameResponse gameResp = facade.createGame(new CreateGameRequest(auth, "Chess4Nerds"));
        System.out.println(gameResp.toString());
        Assertions.assertInstanceOf(Integer.class, gameResp.gameID());
    }

    @Test
    @Order(6)
    public void listGamePos() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);
        String auth = resp.authToken();

        CreateGameResponse gameResp = facade.createGame(new CreateGameRequest(auth, "Chess4Nerds"));
        Assertions.assertInstanceOf(Integer.class, gameResp.gameID());

        ListGameResponse listGame = facade.listGames();
        System.out.println(listGame.toString());
    }

    @Test
    @Order(5)
    public void joinGamePos() throws ResponseException {
        var url = "http://localhost:" + server.port();
        ServerFacade facade = new ServerFacade(url);

        UserModel user = new UserModel("Hello", "password", "henry@gmail.com");
        RegisterResponse resp = facade.registerUser(user);
        String auth = resp.authToken();

        CreateGameResponse gameResp = facade.createGame(new CreateGameRequest(auth, "Chess4Nerds"));
        Assertions.assertInstanceOf(Integer.class, gameResp.gameID());

        facade.joinGame(new JoinGameRequest(auth, "White", gameResp.gameID()));
        ListGameResponse listResp = facade.listGames();
        System.out.println(listResp.toString());
    }

}

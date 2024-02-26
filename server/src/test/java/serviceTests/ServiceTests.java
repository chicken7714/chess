package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import passoffTests.testClasses.TestException;
import request.*;
import response.CreateGameResponse;
import response.ListGameResponse;
import response.LoginResponse;
import response.RegisterResponse;
import service.*;

import java.util.UUID;

public class ServiceTests {

    @Test
    public void positiveRegister() throws UnauthorizedAccessException, InvalidRequestException {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        RegisterService service = new RegisterService();
        RegisterResponse res = service.registerUser(req);

        Assertions.assertEquals(res.username(), "username");
        Assertions.assertInstanceOf(UUID.class, res.authToken());
    }

    @Test
    public void negativeRegister() throws Exception {
        RegisterRequest req1 = new RegisterRequest("username", "password", "email");
        RegisterRequest req2 = new RegisterRequest("username", "password123", "email@email.com");
        RegisterService service = new RegisterService();
        service.registerUser(req1);

        Assertions.assertThrows(UnauthorizedAccessException.class, () -> {
           service.registerUser(req2);
        });
    }

    @Test
    public void positiveLogin() throws Exception {
        RegisterRequest req1 = new RegisterRequest("username", "password", "email");
        RegisterService regService = new RegisterService();
        regService.registerUser(req1);

        LoginRequest req2 = new LoginRequest("username", "password");
        LoginService loginService = new LoginService();
        LoginResponse res = loginService.getUser(req2);
        Assertions.assertEquals(res.username(), "username");
        Assertions.assertInstanceOf(UUID.class, res.authToken());
    }

    @Test
    public void negativeLogin() throws Exception {
        LoginRequest req1 = new LoginRequest("username", "password2");
        LoginService loginService = new LoginService();
        Assertions.assertThrows(RuntimeException.class, () -> {loginService.getUser(req1);});
    }

    @Test
    public void positiveLogout() throws Exception {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        RegisterService service = new RegisterService();
        service.registerUser(req);

        LoginRequest req2 = new LoginRequest("username", "password");
        LoginService loginService = new LoginService();
        LoginResponse res1 = loginService.getUser(req2);

        LogoutRequest req3 = new LogoutRequest(res1.authToken());
        LogoutService logoutService = new LogoutService();
        logoutService.logout(req3);

        AuthDAO authDao = new MemoryAuthDAO();
        Assertions.assertThrows(DataAccessException.class, () -> {authDao.checkValidAuth(res1.authToken());});
    }

    @Test
    public void negativeLogout() throws Exception {
        LogoutRequest req1 = new LogoutRequest(UUID.randomUUID());
        LogoutService service = new LogoutService();
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> {service.logout(req1);});
    }

    @Test
    public void positiveCreateGame() throws Exception {
        RegisterRequest req1 = new RegisterRequest("username", "password", "email");
        RegisterService service = new RegisterService();
        service.registerUser(req1);

        LoginRequest req2 = new LoginRequest("username", "password");
        LoginService loginService = new LoginService();
        LoginResponse res1 = loginService.getUser(req2);

        GameService gameService = new GameService();
        CreateGameRequest req3 = new CreateGameRequest(res1.authToken(), "HelloWorld");
        CreateGameResponse res2 = gameService.createGame(req3);
        Assertions.assertEquals(1, res2.gameID());
    }

    @Test
    public void negativeCreateGame() throws Exception {
        GameService gameService = new GameService();
        CreateGameRequest req3 = new CreateGameRequest(UUID.randomUUID(), "HelloWorld");
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> {gameService.createGame(req3);});
    }

    @Test
    public void positiveJoinGame() throws Exception {
        RegisterRequest req1 = new RegisterRequest("username", "password", "email");
        RegisterService service = new RegisterService();
        service.registerUser(req1);

        LoginRequest req2 = new LoginRequest("username", "password");
        LoginService loginService = new LoginService();
        LoginResponse res1 = loginService.getUser(req2);

        GameService gameService = new GameService();
        CreateGameRequest req3 = new CreateGameRequest(res1.authToken(), "HelloWorld");
        gameService.createGame(req3);

        JoinGameRequest req4 = new JoinGameRequest(res1.authToken(), "WHITE", 1);
        gameService.joinGame(req4);
        GameDAO gameDAO = new MemoryGameDAO();
        var games = gameDAO.listGames();
        Assertions.assertEquals(games.size(), 1);
        Assertions.assertEquals(games.iterator().next().whiteUsername(), "username");
    }

    @Test
    public void negativeJoinGame() throws Exception {
        RegisterRequest regReq1 = new RegisterRequest("username", "password", "email");
        RegisterRequest reqReq2 = new RegisterRequest("username2", "password", "email2");
        RegisterService service = new RegisterService();
        service.registerUser(regReq1);
        service.registerUser(reqReq2);

        LoginRequest loginReq1 = new LoginRequest("username", "password");
        LoginRequest loginReq2 = new LoginRequest("username2", "password");
        LoginService loginService = new LoginService();
        LoginResponse loginRes1 = loginService.getUser(loginReq1);
        LoginResponse loginRes2 = loginService.getUser(loginReq2);

        GameService gameService = new GameService();
        CreateGameRequest req3 = new CreateGameRequest(loginRes1.authToken(), "HelloWorld");
        gameService.createGame(req3);

        JoinGameRequest joinReq1 = new JoinGameRequest(loginRes1.authToken(), "WHITE", 1);
        gameService.joinGame(joinReq1);
        JoinGameRequest joinReq2 = new JoinGameRequest(loginRes2.authToken(), "WHITE", 1);
        Assertions.assertThrows(UnavailableRequestException.class, () -> {gameService.joinGame(joinReq2);});
    }

    @Test
    public void positiveListGames() throws Exception {
        RegisterRequest req1 = new RegisterRequest("username", "password", "email");
        RegisterService service = new RegisterService();
        service.registerUser(req1);

        LoginRequest req2 = new LoginRequest("username", "password");
        LoginService loginService = new LoginService();
        LoginResponse res1 = loginService.getUser(req2);

        GameService gameService = new GameService();
        CreateGameRequest gameReq1 = new CreateGameRequest(res1.authToken(), "HelloWorld");
        CreateGameRequest gameReq2 = new CreateGameRequest(res1.authToken(), "HelloWorld2");
        CreateGameRequest gameReq3 = new CreateGameRequest(res1.authToken(), "HelloWorld3");
        CreateGameRequest gameReq4 = new CreateGameRequest(res1.authToken(), "HelloWorld4");
        CreateGameRequest gameReq5 = new CreateGameRequest(res1.authToken(), "HelloWorld5");
        gameService.createGame(gameReq1);
        gameService.createGame(gameReq2);
        gameService.createGame(gameReq3);
        gameService.createGame(gameReq4);
        gameService.createGame(gameReq5);

        ListGameRequest listReq1 = new ListGameRequest(res1.authToken());

        Assertions.assertEquals(gameService.listGames(listReq1).games().size(), 5);
    }

    @Test
    public void negativeListGames() throws Exception {
        RegisterRequest req1 = new RegisterRequest("username", "password", "email");
        RegisterService service = new RegisterService();
        service.registerUser(req1);

        LoginRequest req2 = new LoginRequest("username", "password");
        LoginService loginService = new LoginService();
        LoginResponse res1 = loginService.getUser(req2);

        GameService gameService = new GameService();
        CreateGameRequest gameReq1 = new CreateGameRequest(res1.authToken(), "HelloWorld");
        CreateGameRequest gameReq2 = new CreateGameRequest(res1.authToken(), "HelloWorld2");
        CreateGameRequest gameReq3 = new CreateGameRequest(res1.authToken(), "HelloWorld3");
        CreateGameRequest gameReq4 = new CreateGameRequest(res1.authToken(), "HelloWorld4");
        CreateGameRequest gameReq5 = new CreateGameRequest(res1.authToken(), "HelloWorld5");
        gameService.createGame(gameReq1);
        gameService.createGame(gameReq2);
        gameService.createGame(gameReq3);
        gameService.createGame(gameReq4);
        gameService.createGame(gameReq5);

        ListGameRequest listReq1 = new ListGameRequest(UUID.randomUUID());

        Assertions.assertThrows(UnauthorizedAccessException.class, () -> {gameService.listGames(listReq1);});
    }

    @Test
    public void clearTest() throws Exception {
        RegisterRequest req1 = new RegisterRequest("username", "password", "email");
        RegisterService service = new RegisterService();
        service.registerUser(req1);

        LoginRequest req2 = new LoginRequest("username", "password");
        LoginService loginService = new LoginService();
        LoginResponse res1 = loginService.getUser(req2);

        GameService gameService = new GameService();
        CreateGameRequest gameReq1 = new CreateGameRequest(res1.authToken(), "HelloWorld");
        CreateGameRequest gameReq2 = new CreateGameRequest(res1.authToken(), "HelloWorld2");
        CreateGameRequest gameReq3 = new CreateGameRequest(res1.authToken(), "HelloWorld3");
        CreateGameRequest gameReq4 = new CreateGameRequest(res1.authToken(), "HelloWorld4");
        CreateGameRequest gameReq5 = new CreateGameRequest(res1.authToken(), "HelloWorld5");
        gameService.createGame(gameReq1);
        gameService.createGame(gameReq2);
        gameService.createGame(gameReq3);
        gameService.createGame(gameReq4);
        gameService.createGame(gameReq5);

        ClearService clearService = new ClearService();
        clearService.clear();

        Assertions.assertThrows(RuntimeException.class, () -> {loginService.getUser(req2);});
        Assertions.assertThrows(UnauthorizedAccessException.class, () -> {gameService.createGame(gameReq1);});

        service.registerUser(req1);
        LoginResponse loginResponse = loginService.getUser(req2);
        ListGameRequest listReq1 = new ListGameRequest(loginResponse.authToken());
        ListGameResponse listGameResponse = gameService.listGames(listReq1);
        Assertions.assertEquals(listGameResponse.games().size(), 0);
    }

}

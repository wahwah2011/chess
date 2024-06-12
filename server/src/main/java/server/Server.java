package server;

import handler.*;
import org.eclipse.jetty.server.Authentication;
import spark.*;
import dataaccess.*;

public class Server {

    private ClearHandler clearHandler;
    private RegisterHandler registerHandler;
    private LoginHandler loginHandler;
    private LogoutHandler logoutHandler;
    private CreateGameHandler createGameHandler;
    private ListGameHandler listGameHandler;
    private JoinGameHandler joinGameHandler;

    public int run(int desiredPort) {

        //create DAOs as instance variables
        UserDAO userData = new SQLUserDAO();
        GameDAO gameData = new SQLGameDAO();
        AuthDAO authData = new SQLAuthDAO();

        clearHandler = new ClearHandler(userData, gameData, authData);
        registerHandler = new RegisterHandler(userData, authData);
        loginHandler = new LoginHandler(authData, userData);
        logoutHandler = new LogoutHandler(authData, userData);
        createGameHandler = new CreateGameHandler(gameData, authData);
        listGameHandler = new ListGameHandler(gameData, authData);
        joinGameHandler = new JoinGameHandler(gameData, authData);

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/wb", WebSocketHandler.class);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", ((request, response) -> registerHandler.handle(request,response)));
        Spark.post("/session", (request, response) -> loginHandler.handle(request, response));
        Spark.post("/game", (request, response) -> createGameHandler.handle(request, response));
        Spark.get("/game", (request, response) -> listGameHandler.handle(request, response));
        Spark.put("/game", (request, response) -> joinGameHandler.handle(request, response));
        Spark.delete("/session", (request, response) -> logoutHandler.handle(request, response));
        Spark.delete("/db", (request, response) -> clearHandler.handle(request, response));

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
        System.out.println("Server stopped successfully.");
    }
}

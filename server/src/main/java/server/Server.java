package server;

import handler.*;
import spark.*;
import dataaccess.*;

public class Server {

    //create DAOs as instance variables
    private MemoryUserDAO userData;
    private MemoryGameDAO gameData;
    private MemoryAuthDAO authData;
    private ClearHandler clearHandler;
    private RegisterHandler registerHandler;
    private LoginHandler loginHandler;
    private LogoutHandler logoutHandler;
    private CreateGameHandler createGameHandler;
    private ListGameHandler listGameHandler;

    public int run(int desiredPort) {

        userData = new MemoryUserDAO();
        gameData = new MemoryGameDAO();
        authData = new MemoryAuthDAO();

        clearHandler = new ClearHandler(userData, gameData, authData);
        registerHandler = new RegisterHandler(userData, authData);
        loginHandler = new LoginHandler(authData, userData);
        logoutHandler = new LogoutHandler(authData, userData);
        createGameHandler = new CreateGameHandler(gameData,authData);
        listGameHandler = new ListGameHandler(gameData,authData);

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", ((request, response) -> registerHandler.handle(request,response)));
        Spark.post("/session", (request, response) -> loginHandler.handle(request, response));
        Spark.post("/game", (request, response) -> createGameHandler.handle(request, response));
        Spark.get("/game", (request, response) -> listGameHandler.handle(request, response));
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

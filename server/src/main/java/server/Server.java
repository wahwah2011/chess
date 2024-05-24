package server;

import handler.ClearHandler;
import handler.RegisterHandler;
import spark.*;
import dataaccess.*;

public class Server {

    //create DAOs as instance variables
    private MemoryUserDAO userData;
    private MemoryGameDAO gameData;
    private MemoryAuthDAO authData;
    private ClearHandler clearHandler;
    private RegisterHandler registerHandler;

    public int run(int desiredPort) {

        userData = new MemoryUserDAO();
        gameData = new MemoryGameDAO();
        authData = new MemoryAuthDAO();

        clearHandler = new ClearHandler(userData, gameData, authData);
        registerHandler = new RegisterHandler(userData, gameData, authData);

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (request, response) -> clearHandler.handle(request, response));
        Spark.post("/user", ((request, response) -> registerHandler.handle(request,response)));

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

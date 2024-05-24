package server;

import handler.ClearHandler;
import spark.*;
import dataaccess.*;

public class Server {

    //create DAOs as instance variables
    private MemoryUserDAO userData;
    private MemoryGameDAO gameData;
    private MemoryAuthDAO authData;
    private ClearHandler clearHandler;

    public int run(int desiredPort) {
        clearHandler = new ClearHandler(userData, gameData, authData);

        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.init();
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (request, response) -> clearHandler.clearRequest(request, response));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

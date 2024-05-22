package server;

import spark.*;
import dataaccess.*;

public class Server {

    //create DAOs as instance variables
    private MemoryUserDAO userData;
    private MemoryGameDAO gameData;
    private MemoryAuthDAO authData;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        // pass whatever data

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

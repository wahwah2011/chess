package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {

    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public CreateGameHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        //takes authtoken and body containing GameData object containing gameName

        GameService createGameService = new GameService(authDAO,gameDAO);
        Gson serializer = new Gson();
        GameData result;

        // get the authToken string from the header of the incoming http file
        AuthData auth = new AuthData(request.headers("authorization"), null, null);
        GameData newGame = serializer.fromJson(request.body(), GameData.class);

        //check if bad request
        if ((auth.authToken() == null) || (newGame.gameName() == null)) {
            response.status(400); // Internal Server Error
            AuthData badResult = new AuthData(null,null,"Error: bad request");
            return serializer.toJson(badResult);
        }

        result = createGameService.createGame(auth,newGame);

        if (result.message() != null) {
            String message = result.message();
            if (message.equals("Error: unauthorized")) {
                response.status(401);
            }
            else response.status(500);
        }
        else {
            response.status(200); // OK
        }

        //returns GameData containing gameID
        return serializer.toJson(result);
    }

}

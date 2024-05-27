package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListGameHandler implements Route {

    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public ListGameHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        GameService listGameService = new GameService(authDAO,gameDAO);
        Gson serializer = new Gson();
        GameList result;

        AuthData auth = new AuthData(request.headers("authorization"), null, null);

        result = listGameService.listGames(auth);

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

        return serializer.toJson(result);
    }
}

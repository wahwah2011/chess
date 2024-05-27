package handler;

import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import service.GameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {

    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public JoinGameHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {

        GameService joinGameService = new GameService(authDAO,gameDAO);
        Gson serializer = new Gson();
        AuthData result;

        //Gson s
        //AuthData auth = header
        //JoinRequest joinRequest =
        AuthData auth = new AuthData(request.headers("authorization"), null, null);
        JoinRequest joinRequest = serializer.fromJson(request.body(), JoinRequest.class);

        //check and see if joinrequest.color() is equal to "BLACK" or "WHITE"
        if (joinRequest.playerColor() != null) {
            if (!joinRequest.playerColor().equals("BLACK") && !joinRequest.playerColor().equals("WHITE")) {
                response.status(400); // Internal Server Error
                AuthData badResult = new AuthData(null,null,"Error: bad request");
                return serializer.toJson(badResult);
            }
        }
        else {
            response.status(400); // Internal Server Error
            AuthData badResult = new AuthData(null,null,"Error: bad request");
            return serializer.toJson(badResult);
        }

        result = joinGameService.joinGame(auth,joinRequest);

        if (result.message() != null) {
            String message = result.message();
            if (message.equals("Error: unauthorized")) {
                response.status(401);
            }
            else if (message.equals("Error: already taken")) {
                response.status(403);
            }
            else if (message.equals("Error: bad request")) {
                response.status(400);
            }
            else response.status(500);
        }
        else {
            response.status(200); // OK
        }

        return serializer.toJson(result);
    }
}

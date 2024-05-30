package handler;

import com.google.gson.Gson;
import dataaccess.*;
import handler.json.*;
import model.ClearResponse;
import server.Server;
import service.Clear;
import spark.*;


public class ClearHandler implements Route {
    private final UserDAO userData;
    private final GameDAO gameData;
    private final AuthDAO authData;

    public ClearHandler(UserDAO userData, GameDAO gameData, AuthDAO authData) {
        this.userData = userData;
        this.gameData = gameData;
        this.authData = authData;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Gson serializer = new Gson();
        Clear clear = new Clear(authData, userData, gameData);
        ClearResponse clearObj = clear.clearGame();
        response.status(200);
        return serializer.toJson(clearObj);
    }
}

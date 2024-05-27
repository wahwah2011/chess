package handler;

import com.google.gson.Gson;
import dataaccess.*;
import handler.json.*;
import model.Response.ClearResponse;
import server.Server;
import service.Clear;
import spark.*;


public class ClearHandler implements Route {
    private final MemoryUserDAO userData;
    private final MemoryGameDAO gameData;
    private final MemoryAuthDAO authData;

    public ClearHandler(MemoryUserDAO userData, MemoryGameDAO gameData, MemoryAuthDAO authData) {
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

package handler;

import com.google.gson.Gson;
import dataaccess.*;
import handler.json.*;
import model.Response.ClearResponse;
import server.Server;
import service.Clear;
import spark.*;


public class ClearHandler extends Server {
    private MemoryUserDAO userData;
    private MemoryGameDAO gameData;
    private MemoryAuthDAO authData;

    public ClearHandler(MemoryUserDAO userData, MemoryGameDAO gameData, MemoryAuthDAO authData) {
        this.userData = userData;
        this.gameData = gameData;
        this.authData = authData;
    }

    //shouldn't take anything in
    public Object clearRequest(Request req, Response res) {
        Gson serializer = new Gson();
        Clear clear = new Clear(authData, userData, gameData);
        ClearResponse clearObj = clear.clearGame();
        String json = serializer.toJson(clearObj);
        return json;
    }



}

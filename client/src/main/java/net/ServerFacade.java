package net;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;

public class ServerFacade {

    private String urlString = "http://localhost:8080";
    private ClientCommunicator communicator = new ClientCommunicator();

    public static String USER_PATH = "/user";
    public static String SESSION_PATH = "/session";
    public static String GAME_PATH = "/game";
    public static String DB_PATH = "/db";

    public AuthData registerFacade(String username, String password, String email) throws IOException {
        UserData registerRequest = new UserData(username,password,email);
        Gson serializer = new Gson();
        String json = serializer.toJson(registerRequest);
        String response = communicator.doPost(urlString,USER_PATH,json);
        AuthData registerResponse = serializer.fromJson(response, AuthData.class);
        return registerResponse;
    }

    public AuthData login(String username, String password) throws IOException {
        UserData loginRequest = new UserData(username, password, null);
        Gson serializer = new Gson();
        String json = serializer.toJson(loginRequest);
        String response = communicator.doPost(urlString,SESSION_PATH,json);
        AuthData loginResponse = serializer.fromJson(response, AuthData.class);
        return loginResponse;
    }

    public void logout(String authToken) throws IOException {
        String response = communicator.doDelete(urlString,SESSION_PATH,authToken);
    }

    public GameList listGames(AuthData authorization) {return null;}

    public GameData createGame(AuthData authorization, GameData gameData) {return null;}

    public AuthData joinGame(AuthData authorization, JoinRequest joinRequest) { return null;}

    public void clear() throws IOException {
        String response = communicator.doDelete(urlString, DB_PATH,null);
    }

    private String serializer(Object o) {
        Gson serializer = new Gson();
        return serializer.toJson(o);
    }

}

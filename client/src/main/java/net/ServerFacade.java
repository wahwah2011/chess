package net;

import chess.ChessGame;
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
        String response = communicator.doPost(urlString,USER_PATH,json,null);
        AuthData registerResponse = serializer.fromJson(response, AuthData.class);
        return registerResponse;
    }

    public AuthData login(String username, String password) throws IOException {
        UserData loginRequest = new UserData(username, password, null);
        Gson serializer = new Gson();
        String json = serializer.toJson(loginRequest);
        String response = communicator.doPost(urlString,SESSION_PATH,json, null);
        AuthData loginResponse = serializer.fromJson(response, AuthData.class);
        return loginResponse;
    }

    public AuthData logout(String authToken) throws IOException {
        String result = communicator.doDelete(urlString,SESSION_PATH,authToken);
        Gson serializer = new Gson();
        AuthData logoutResult = serializer.fromJson(result,AuthData.class);
        return logoutResult;
    }

    public GameList listGames(String authToken) throws IOException {
        Gson serializer = new Gson();
        String response = communicator.doGet(urlString,GAME_PATH, authToken);
        GameList listGamesResponse = serializer.fromJson(response, GameList.class);
        return listGamesResponse;
    }

    public GameData createGame(String authToken, String gameName) throws IOException {
        GameData createGameRequest = new GameData(null,null,null,gameName,null,null);
        Gson serializer = new Gson();
        String json = serializer.toJson(createGameRequest);
        String response = communicator.doPost(urlString,GAME_PATH,json,authToken);
        GameData createGameResponse = serializer.fromJson(response,GameData.class);
        return createGameResponse;
    }

    public AuthData joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws IOException {
        JoinRequest joinRequest = new JoinRequest(playerColor,gameID);
        Gson serializer = new Gson();
        String json = serializer.toJson(joinRequest);
        String response = communicator.doPut(urlString, GAME_PATH, authToken, json);
        AuthData joinGameResponse = serializer.fromJson(response, AuthData.class);
        return joinGameResponse;
    }

    public void clear() throws IOException {
        communicator.doDelete(urlString, DB_PATH,null);
    }

    private String serializer(Object o) {
        Gson serializer = new Gson();
        return serializer.toJson(o);
    }

    private Object deserializer(String json, Object o) {
        Gson serializer = new Gson();
        return serializer.fromJson(json, o.getClass());
    }

}

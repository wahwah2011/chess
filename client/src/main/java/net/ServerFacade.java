package net;

import chess.ChessGame;
import client.ChessClient;
import com.google.gson.Gson;
import model.*;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class ServerFacade {

    private ChessClient client;
    private final String urlString;
    private final ClientCommunicator clientCommunicator = new ClientCommunicator();
    private final WebsocketCommunicator websocketCommunicator;

    public static final String USER_PATH = "/user";
    public static final String SESSION_PATH = "/session";
    public static final String GAME_PATH = "/game";
    public static final String DB_PATH = "/db";

    //for testing serverfacade without websocket
    public ServerFacade(int port) {
        this.urlString = "http://localhost:" + port;
        websocketCommunicator = null;
    }

    public ServerFacade(int port, ChessClient client) {
        this.urlString = "http://localhost:" + port;
        this.client = client;
        try {
            websocketCommunicator = new WebsocketCommunicator(client);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public AuthData registerFacade(String username, String password, String email) throws IOException {
        UserData registerRequest = new UserData(username,password,email);
        Gson serializer = new Gson();
        String json = serializer.toJson(registerRequest);
        String response = clientCommunicator.doPost(urlString,USER_PATH,json,null);
        AuthData registerResponse = serializer.fromJson(response, AuthData.class);
        return registerResponse;
    }

    public AuthData login(String username, String password) throws IOException {
        UserData loginRequest = new UserData(username, password, null);
        Gson serializer = new Gson();
        String json = serializer.toJson(loginRequest);
        String response = clientCommunicator.doPost(urlString,SESSION_PATH,json, null);
        AuthData loginResponse = serializer.fromJson(response, AuthData.class);
        return loginResponse;
    }

    public AuthData logout(String authToken) throws IOException {
        String result = clientCommunicator.doDelete(urlString,SESSION_PATH,authToken);
        Gson serializer = new Gson();
        AuthData logoutResult = serializer.fromJson(result,AuthData.class);
        return logoutResult;
    }

    public GameList listGames(String authToken) throws IOException {
        Gson serializer = new Gson();
        String response = clientCommunicator.doGet(urlString,GAME_PATH, authToken);
        GameList listGamesResponse = serializer.fromJson(response, GameList.class);
        return listGamesResponse;
    }

    public GameData createGame(String authToken, String gameName) throws IOException {
        GameData createGameRequest = new GameData(null,null,null,gameName,null,null);
        Gson serializer = new Gson();
        String json = serializer.toJson(createGameRequest);
        String response = clientCommunicator.doPost(urlString,GAME_PATH,json,authToken);
        GameData createGameResponse = serializer.fromJson(response,GameData.class);
        return createGameResponse;
    }

    public AuthData joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws IOException {
        JoinRequest joinRequest = new JoinRequest(playerColor,gameID);
        Gson serializer = new Gson();
        String json = serializer.toJson(joinRequest);
        String response = clientCommunicator.doPut(urlString, GAME_PATH, authToken, json);
        AuthData joinGameResponse = serializer.fromJson(response, AuthData.class);
        ConnectCommand connectCommand = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        String command = serializer.toJson(connectCommand);
        if (joinGameResponse.message() == null) {
            try {
                websocketCommunicator.send(command);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return joinGameResponse;
    }

    public void observeGame(String authToken, int gameID) {
        ConnectCommand connectCommand = new ConnectCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        Gson serializer = new Gson();
        String command = serializer.toJson(connectCommand);
        try {
            websocketCommunicator.send(command);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void leaveGame(String authToken, ChessGame.TeamColor playerColor, int gameID) throws IOException {
        JoinRequest leaveRequest = new JoinRequest(playerColor, gameID);
        Gson serializer = new Gson();
        String json = serializer.toJson(leaveRequest);
        clientCommunicator.doPut(urlString, GAME_PATH, authToken, json);
    }

    public void clear() throws IOException {
        clientCommunicator.doDelete(urlString, DB_PATH,null);
    }
}

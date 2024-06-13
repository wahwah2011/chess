package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.*;
import model.GameData;
import model.JoinRequest;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@WebSocket
public class WebSocketHandler {

    private final Map<Integer, HashSet<Session>> sessionMap = new HashMap<>(); //gameID -> session map
    private final SQLAuthDAO authData = new SQLAuthDAO();
    private final SQLGameDAO gameData = new SQLGameDAO();

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(UserGameCommand.class, new UserGameCommandDeserializer())
                    .create();

            UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);

            String username = authData.getUsername(command.getAuthString());

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            sendRemoteMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            sendRemoteMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex.getMessage()));
        }
    }


    private void connect(Session session, String username, ConnectCommand command) throws DataAccessException {
        saveSession(command.getGameID(), session);
        GameData game = gameData.getGame(command.getGameID());
        String chessGame = serializeGame(game.game());
        LoadGameMessage loadGameMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
        System.out.println("Inside of connect");
        sendRemoteMessage(session, loadGameMessage);

        String connectNotification = generateConnectNotification(username,game);
        NotificationMessage notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, connectNotification);

        broadcast(session, notification, game.gameID());
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws DataAccessException {

    }

    private void leaveGame(Session session, String username, LeaveGameCommand command) throws DataAccessException {
        removeSession(command.getGameID(), session);
    }

    private void resign(Session session, String username, ResignCommand command) throws DataAccessException {

    }

    private void saveSession(Integer gameID, Session session) {
        sessionMap.computeIfAbsent(gameID, k -> new HashSet<>()).add(session);
    }

    private void removeSession(Integer gameID, Session session) {
        sessionMap.remove(gameID, session);
    }

    private void broadcast(Session session, NotificationMessage notificationMessage, Integer gameID) {
        HashSet<Session> sessions = sessionMap.get(gameID);
        Gson serializer = new Gson();
        String json = serializer.toJson(notificationMessage);
        for (Session s : sessions) {
            if (!s.equals(session)){
                try {
                    send(json, s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void sendRemoteMessage(Session session, Object message) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);

        try {
            send(jsonMessage, session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String serializeGame(ChessGame game) {
        Gson serializer = new Gson();
        return serializer.toJson(game);
    }

    private String generateConnectNotification(String username, GameData game) {
        if (game.blackUsername() != null) {
            if (game.blackUsername().equals(username)) {
                return ("Player " + username + " joined as player on black team");
            }
        }
        else if (game.whiteUsername() != null) {
            if (game.whiteUsername().equals(username)) {
                return ("Player " + username + " joined as player on white team");
            }
        }
        return ("Player " + username + " joined as observer");
    }

    public void send(String msg, Session session) throws IOException {
        session.getRemote().sendString(msg);
    }
}


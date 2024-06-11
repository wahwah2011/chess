package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@WebSocket
public class WebSocketHandler {

    private Map<Integer, HashSet<Session>> sessionMap = new HashMap(); //gameID -> session map

    public static void main(String[] args) {
        Spark.port(8080);
        Spark.webSocket("/ws", WebSocketHandler.class);
        Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        try {
            Gson serializer = new Gson();
            UserGameCommand command = serializer.fromJson(msg, UserGameCommand.class);

            String username = getUsername(command.getAuthString());

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (DataAccessException ex) {
            //sendMessage(session.getRemote(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized"));
        } catch (Exception ex) {
            ex.printStackTrace();
            //sendMessage(session.getRemote(), new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, ConnectCommand command) throws DataAccessException {}
    private void makeMove(Session session, String username, MakeMoveCommand command) throws DataAccessException {}
    private void leaveGame(Session session, String username, LeaveGameCommand command) throws DataAccessException {}
    private void resign(Session session, String username, ResignCommand command) throws DataAccessException {}

    private void saveSession(Integer gameID, Session session) {
        sessionMap.computeIfAbsent(gameID, k -> new HashSet<>()).add(session);
    }

    private void broadcast(NotificationMessage notificationMessage) {}

    private String getUsername(String authToken) {
        return null;
    }

    private void sendMessage(Session session, Object message) {
        Gson gson = new Gson();
        String jsonMessage = gson.toJson(message);

        try {
            session.getRemote().sendString(jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
    }
        }

}


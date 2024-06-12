package net;

import client.ChessClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.messages.*;

import javax.websocket.*;
import java.net.URI;

public class WebsocketCommunicator extends Endpoint {
    private final ChessClient observer;
    private final Session session;

    public WebsocketCommunicator(ChessClient client) throws Exception {
        this.observer = client;


        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            // Ensure this URI is correct
            URI uri = new URI("ws://localhost:8080/ws");
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer())
                            .create();

                    try {
                        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                        observer.notify(serverMessage);
                    } catch (Exception ex) {
                        observer.notify(new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage()));
                    }
                }

            });
        } catch (DeploymentException e) {
            throw new RuntimeException("Failed to connect to the WebSocket server. Ensure the server is running and the URI is correct.", e);
        }
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println("Connected to the WebSocket server.");
    }

    @Override
    public void onError(Session session, Throwable thr) {
        System.err.println("WebSocket error: " + thr.getMessage());
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("WebSocket connection closed: " + closeReason.getReasonPhrase());
    }
}

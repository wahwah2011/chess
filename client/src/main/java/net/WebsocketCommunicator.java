package net;

import client.ChessClient;

public class WebsocketCommunicator {
    private ChessClient client;

    public WebsocketCommunicator(ChessClient client) {
        this.client = client;
    }
}

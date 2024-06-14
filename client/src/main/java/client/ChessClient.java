package client;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import net.ServerFacade;
import ui.InGameUI;
import ui.PostLoginUI;
import ui.PreLoginUI;
import ui.chessboard.DrawBoard;
import websocket.messages.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient implements ServerMessageObserver {
    private boolean isLoggedIn = false;
    private boolean isInGame = false;
    private boolean observingGame = false;
    private String authToken = null;
    private String gameName = null;
    private Integer gameID = null;
    private final ServerFacade serverFacade;
    private String teamColor = null;
    private ChessGame chessGame = null;

    public ChessClient(int port) {
        serverFacade = new ServerFacade(port, this);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        PreLoginUI preLoginUI = new PreLoginUI(this, serverFacade, scanner);
        PostLoginUI postLoginUI = new PostLoginUI(this, serverFacade, scanner);
        InGameUI inGameUI = new InGameUI(this, serverFacade, scanner);

        while (true) {
            if (!isLoggedIn()) {
                preLoginUI.run();
            }
            else if (!isInGame) {
                postLoginUI.run();
            }
            else {
                inGameUI.run();
            }
        }
    }


    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    public ChessBoard getBoard() {
        return chessGame.getBoard();
    }

    public void setObservingGame(boolean observingGame) {
        this.observingGame = observingGame;
    }

    private boolean isLoggedIn() {
        return this.isLoggedIn && this.authToken != null;
    }

    public void authMessage(AuthData auth) {
        if (auth.message() != null) {
            printErrorMessage(auth.message());
        }
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> printNotification(((NotificationMessage) message).getMessage());
            case ERROR -> printErrorMessage(((ErrorMessage) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGameMessage) message).getGame());
        }
    }

    public void loadGame(String game) {
        Gson serializer = new Gson();
        ChessGame chessGame = serializer.fromJson(game, ChessGame.class);
        this.chessGame = chessGame;
        DrawBoard board = new DrawBoard(chessGame.getBoard());
        if (this.teamColor != null) {
            board.drawChessBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), this.teamColor);
        }
        else board.drawObserverView(new PrintStream(System.out, true, StandardCharsets.UTF_8));
    }

    public void printNotification(String message) {
        System.out.print(SET_TEXT_COLOR_GREEN);
        printMessage(message);
    }

    public void printErrorMessage(String message) {
        System.out.print(SET_TEXT_COLOR_RED);
        printMessage(message);
    }

    private void printMessage(String message) {
        System.out.println("\n" + message);
        System.out.print(SET_TEXT_COLOR_BLACK);
        System.out.print("\n");
    }

}

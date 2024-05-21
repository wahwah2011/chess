package service;

import model.UserData;

//receives a request object
public class JoinGame {
    private UserData userData;

    public JoinGame(UserData user) {
        this.userData = user;
    }
}

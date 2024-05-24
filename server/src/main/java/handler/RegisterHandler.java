package handler;

import com.google.gson.Gson;
import dataaccess.*;
import handler.json.ErrorResponse;
import model.AuthData;
import model.UserData;
import server.Server;
import service.UserService;
import spark.*;

public class RegisterHandler extends Server {
    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    public RegisterHandler(MemoryUserDAO userData, MemoryGameDAO gameData, MemoryAuthDAO authData) {
        this.userDAO = userData;
        this.authDAO = authData;
    }

    public Object registerRequest(Request req, Response res) {

        UserService registerService = new UserService(userDAO, authDAO);
        Gson serializer = new Gson();
        UserData newUser;
        AuthData result;

        // Deserialize the request body to UserData
        newUser = serializer.fromJson(req.body(), UserData.class);

        result = registerService.register(newUser);


        if (result.message() != null) {
            String message = result.message();
            if (message.equals("Error: already taken")) {
                res.status(403); // Already taken
            }
            else if (message.equals("Error: bad request")) {
                res.status(400); // Internal Server Error
            }
            else res.status(500);
        }
        else {
            res.status(200); // OK
        }

        // Successful registration
        return serializer.toJson(result);
    }

}

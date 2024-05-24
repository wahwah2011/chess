package handler;

import com.google.gson.Gson;
import dataaccess.*;
import handler.json.ErrorResponse;
import model.AuthData;
import model.UserData;
import server.Server;
import service.UserService;
import spark.*;

public class RegisterHandler implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        UserService registerService = new UserService(userDAO, authDAO);
        Gson serializer = new Gson();
        UserData newUser;
        AuthData result;

        // Deserialize the request body to UserData
        newUser = serializer.fromJson(request.body(), UserData.class);

        result = registerService.register(newUser);


        if (result.message() != null) {
            String message = result.message();
            if (message.equals("Error: already taken")) {
                response.status(403); // Already taken
            }
            else if (message.equals("Error: bad request")) {
                response.status(400); // Internal Server Error
            }
            else response.status(500);
        }
        else {
            response.status(200); // OK
        }

        // Successful registration
        return serializer.toJson(result);
    }

    private MemoryUserDAO userDAO;
    private MemoryAuthDAO authDAO;

    public RegisterHandler(MemoryUserDAO userData, MemoryGameDAO gameData, MemoryAuthDAO authData) {
        this.userDAO = userData;
        this.authDAO = authData;
    }

}

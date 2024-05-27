package handler;

import com.google.gson.Gson;
import dataaccess.*;
import handler.json.ErrorResponse;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import server.Server;
import service.UserService;
import spark.*;

public class RegisterHandler implements Route {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterHandler(UserDAO userData, AuthDAO authData) {
        this.userDAO = userData;
        this.authDAO = authData;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        UserService registerService = new UserService(userDAO, authDAO);
        Gson serializer = new Gson();
        UserData newUser;
        AuthData result;

        // Deserialize the request body to UserData
        newUser = serializer.fromJson(request.body(), UserData.class);

        //check for bad request (if one of the variables is null)
        if (newUser.username() == null || newUser.password() == null || newUser.email() == null) {
            response.status(400); // Internal Server Error
            result = new AuthData(null,null,"Error: bad request");
            return serializer.toJson(result);
        }

        result = registerService.register(newUser);

        if (result.message() != null) {
            String message = result.message();
            if (message.equals("Error: already taken")) {
                response.status(403); // Already taken
            }
            else response.status(500);
        }
        else {
            response.status(200); // OK
        }

        // Successful registration
        return serializer.toJson(result);
    }

}

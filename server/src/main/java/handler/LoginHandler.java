package handler;

import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginHandler(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        UserService registerService = new UserService(userDAO, authDAO);
        Gson serializer = new Gson();
        UserData newUser;
        AuthData result;

        // Deserialize the request body to UserData
        newUser = serializer.fromJson(request.body(), UserData.class);

        //check if bad request
        if (newUser.username() == null || newUser.password() == null) {
            response.status(400); // Internal Server Error
            result = new AuthData(null,null,"Error: bad request");
            return serializer.toJson(result);
        }

        result = registerService.login(newUser);

        if (result.message() != null) {
            String message = result.message();
            if (message.equals("Error: unauthorized")) {
                response.status(401); // Already taken
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

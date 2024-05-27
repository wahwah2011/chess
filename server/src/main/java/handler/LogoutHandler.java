package handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LogoutHandler(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        UserService registerService = new UserService(userDAO, authDAO);
        Gson serializer = new Gson();
        AuthData result;

        // get the authToken string from the header of the incoming http file
        AuthData auth = new AuthData(request.headers("authorization"), null, null);

        //check if bad request
        if (auth.authToken() == null) {
            response.status(400); // Internal Server Error
            result = new AuthData(null,null,"Error: bad request");
            return serializer.toJson(result);
        }

        result = registerService.logout(auth);

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

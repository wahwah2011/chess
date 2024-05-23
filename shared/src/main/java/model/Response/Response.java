package model.Response;

public class Response {
    boolean success;
    String message;

    public Response() {}

    public Response(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

}

package handler.json;

import com.google.gson.Gson;

public class ToJson {
    private final Object responseObj;

    public ToJson(Object responseObj) {
        this.responseObj = responseObj;
    }

    public String toJson() {
        Gson gson = new Gson();

        String json = gson.toJson(responseObj);
        return json;
    }
}

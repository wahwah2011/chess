package handler.json;

import com.google.gson.Gson;
import spark.*;

public class FromJson {
    private Request json;

    public FromJson(Request json) {
        this.json = json;
    }

    public Object fromJson() {
        Gson serializer = new Gson();

        String json = serializer.toJson(this.json);
        Object objFromJson = serializer.fromJson(String.valueOf(this.json), Object.class);
        return objFromJson;
    }
}

package websocket.messages;

import com.google.gson.*;

import java.lang.reflect.Type;

public class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {
    @Override
    public ServerMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String messageTypeString = jsonObject.get("serverMessageType").getAsString();
        ServerMessage.ServerMessageType messageType = ServerMessage.ServerMessageType.valueOf(messageTypeString);

        Class<? extends ServerMessage> messageClass;
        switch (messageType) {
            case NOTIFICATION:
                messageClass = NotificationMessage.class;
                break;
            case ERROR:
                messageClass = ErrorMessage.class;
                break;
            case LOAD_GAME:
                messageClass = LoadGameMessage.class;
                break;
            default:
                throw new JsonParseException("Unknown message type: " + messageType);
        }

        return context.deserialize(json, messageClass);
    }
}

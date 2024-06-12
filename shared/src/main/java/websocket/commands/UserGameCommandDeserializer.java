package websocket.commands;

import com.google.gson.*;

import java.lang.reflect.Type;

public class UserGameCommandDeserializer implements JsonDeserializer<UserGameCommand> {
    @Override
    public UserGameCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String commandTypeString = jsonObject.get("commandType").getAsString();
        UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(commandTypeString);

        Class<? extends UserGameCommand> commandClass;
        switch (commandType) {
            case CONNECT:
                commandClass = ConnectCommand.class;
                break;
            case MAKE_MOVE:
                commandClass = MakeMoveCommand.class;
                break;
            case LEAVE:
                commandClass = LeaveGameCommand.class;
                break;
            case RESIGN:
                commandClass = ResignCommand.class;
                break;
            default:
                throw new JsonParseException("Unknown command type: " + commandType);
        }

        return context.deserialize(json, commandClass);
    }
}

package websocket.messages;

import com.google.gson.*;
import java.lang.reflect.Type;

public class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {
    @Override
    public ServerMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String type = obj.get("serverMessageType").getAsString();

        switch (type) {
            case "NOTIFICATION":
                return context.deserialize(obj, NotificationMessage.class);
            case "LOAD_GAME":
                return context.deserialize(obj, LoadGameMessage.class);
            case "ERROR":
                return context.deserialize(obj, ErrorMessage.class);
            default:
                throw new JsonParseException("Unknown message type: " + type);
        }
    }
}

package it.polimi.ingsw.utility;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import it.polimi.ingsw.model.action.Action;
import it.polimi.ingsw.model.action.ConstructAction;
import it.polimi.ingsw.model.action.GeneralAction;
import it.polimi.ingsw.model.action.MoveAction;

import java.lang.reflect.Type;

public class ActionDeserializer implements JsonDeserializer<Action> {
    @Override
    public Action deserialize(JsonElement json, Type member, JsonDeserializationContext context) {
        String actionType = json.getAsJsonObject().get("actionType").getAsString();
        switch (actionType) {
            case "MOVE": return context.deserialize(json, MoveAction.class);
            case "CONSTRUCT": return context.deserialize(json, ConstructAction.class);
            case "GENERAL": return context.deserialize(json, GeneralAction.class);
            default: return null;
        }
    }
}

package tasks.serializers;

import com.google.gson.*;
import managers.TaskManager;
import tasks.Epic;
import tasks.Statuses;
import tasks.TypeOfTasks;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EpicDeserializer implements JsonDeserializer<Epic> {
    private final TaskManager manager;

    public EpicDeserializer(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public Epic deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        Epic epic;
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        if (jsonObject.has("id")) {
            int id = jsonObject.get("id").getAsInt();
            Statuses status = Statuses.valueOf(jsonObject.get("status").getAsString().toUpperCase());
            TypeOfTasks typeOfTasks = TypeOfTasks.valueOf(jsonObject.get("type").getAsString().toUpperCase());
            String description = null;
            if (jsonObject.has("description")) {
                description = jsonObject.get("description").getAsString();
            }
            epic = new Epic(id, name, description, status, typeOfTasks, null, null);
            if (jsonObject.has("subtask list")) {
                epic.setSubtaskList(manager.getSubtasksFromEpic(id));
            } else {
                epic = new Epic(id, name, description, status, typeOfTasks, null, null);
            }
        } else {
            epic = new Epic(name, new ArrayList<>());
        }
        return epic;
    }
}
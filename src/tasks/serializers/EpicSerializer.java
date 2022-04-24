package tasks.serializers;

import com.google.gson.*;
import tasks.Epic;
import tasks.Subtask;

import java.lang.reflect.Type;

public class EpicSerializer implements JsonSerializer<Epic> {

    @Override
    public JsonElement serialize(Epic epic, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", epic.getId());
        result.addProperty("name", epic.getName());
        result.addProperty("description", epic.getDescription());
        result.addProperty("status", String.valueOf(epic.getStatus()));
        result.addProperty("type", String.valueOf(epic.getType()));
        JsonArray subtaskList = new JsonArray();
        if (!epic.getSubtaskList().isEmpty()) {
            for (Subtask subtask : epic.getSubtaskList()) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Subtask.class, new SubtaskSerializer())
                        .create();
                subtaskList.add(gson.toJson(subtask));
            }
            result.addProperty("start time", epic.getStartTime().format(epic.getFormatterTime()));
            result.addProperty("end time", epic.getEndTime().format(epic.getFormatterTime()));
            result.addProperty("duration", epic.getDuration().toHours() + "h." + epic.getDuration().toMinutesPart() + "m.");
            result.add("subtask list", subtaskList);
        }
        return result;
    }
}
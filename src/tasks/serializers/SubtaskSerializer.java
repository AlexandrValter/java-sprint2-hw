package tasks.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import tasks.Subtask;

import java.lang.reflect.Type;

public class SubtaskSerializer implements JsonSerializer<Subtask> {
    @Override
    public JsonElement serialize(Subtask subtask, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", subtask.getId());
        result.addProperty("name", subtask.getName());
        result.addProperty("description", subtask.getDescription());
        result.addProperty("status", String.valueOf(subtask.getStatus()));
        result.addProperty("type", String.valueOf(subtask.getType()));
        result.addProperty("start time", subtask.getStartTime().format(subtask.getFormatterTime()));
        result.addProperty("end time", subtask.getEndTime().format(subtask.getFormatterTime()));
        result.addProperty("duration", subtask.getDuration().toHours() + "h." + subtask.getDuration().toMinutesPart() + "m.");
        result.addProperty("epic id", subtask.getEpic().getId());
        return result;
    }
}

package tasks.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import tasks.Task;

import java.lang.reflect.Type;

public class TaskSerializer implements JsonSerializer<Task> {
    @Override
    public JsonElement serialize(Task task, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.addProperty("id", task.getId());
        result.addProperty("name", task.getName());
        result.addProperty("description", task.getDescription());
        result.addProperty("status", String.valueOf(task.getStatus()));
        result.addProperty("type", String.valueOf(task.getType()));
        result.addProperty("start time", task.getStartTime().format(task.getFormatterTime()));
        result.addProperty("end time", task.getEndTime().format(task.getFormatterTime()));
        result.addProperty("duration", task.getDuration().toHours() + "h." + task.getDuration().toMinutesPart() + "m.");
        return result;
    }
}
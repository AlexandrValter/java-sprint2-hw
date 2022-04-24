package tasks.serializers;

import com.google.gson.*;
import tasks.Statuses;
import tasks.Task;
import tasks.TypeOfTasks;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskDeserializer implements JsonDeserializer<Task> {
    private final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public Task deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        Task task = null;
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String description = jsonObject.get("description").getAsString();
            if (jsonObject.has("id")) {
                int id = jsonObject.get("id").getAsInt();
                Statuses status = Statuses.valueOf(jsonObject.get("status").getAsString().toUpperCase());
                TypeOfTasks typeOfTasks = TypeOfTasks.valueOf(jsonObject.get("type").getAsString().toUpperCase());
                LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("start time").getAsString(), formatterTime);
                LocalDateTime endTime = LocalDateTime.parse(jsonObject.get("end time").getAsString(), formatterTime);
                task = new Task(id, name, description, status, typeOfTasks, startTime, Duration.between(startTime, endTime));
            } else {
                String endTime = jsonObject.get("start time").getAsString();
                int duration = jsonObject.get("duration").getAsInt();
                task = new Task(name, description, endTime, duration);
            }
        }
        return task;
    }
}
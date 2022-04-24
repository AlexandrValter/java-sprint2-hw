package tasks.serializers;

import com.google.gson.*;
import managers.TaskManager;
import tasks.Statuses;
import tasks.Subtask;
import tasks.TypeOfTasks;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubtaskDeserializer implements JsonDeserializer<Subtask> {
    private final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final TaskManager manager;

    public SubtaskDeserializer(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public Subtask deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        Subtask subtask = null;
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            int epicId = jsonObject.get("epic id").getAsInt();
            if (jsonObject.has("id")) {
                int id = jsonObject.get("id").getAsInt();
                Statuses status = Statuses.valueOf(jsonObject.get("status").getAsString().toUpperCase());
                TypeOfTasks typeOfTasks = TypeOfTasks.valueOf(jsonObject.get("type").getAsString().toUpperCase());
                LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("start time").getAsString(), formatterTime);
                LocalDateTime endTime = LocalDateTime.parse(jsonObject.get("end time").getAsString(), formatterTime);
                String description = null;
                if (jsonObject.has("description")) {
                    description = jsonObject.get("description").getAsString();
                }
                subtask = new Subtask(id, name, description, status, typeOfTasks, startTime, Duration.between(startTime, endTime));
                subtask.setEpic(manager.getEpic(epicId));
            } else {
                String endTime = jsonObject.get("start time").getAsString();
                int duration = jsonObject.get("duration").getAsInt();
                subtask = new Subtask(name, manager.getEpic(epicId), endTime, duration);
            }
        }
        return subtask;
    }
}
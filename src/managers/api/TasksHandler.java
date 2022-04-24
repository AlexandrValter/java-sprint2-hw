package managers.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.*;
import tasks.serializers.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TasksHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TasksHandler(TaskManager manager) {
        this.manager = manager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Task.class, new TaskSerializer())
                .registerTypeAdapter(Subtask.class, new SubtaskSerializer())
                .registerTypeAdapter(Epic.class, new EpicSerializer())
                .registerTypeAdapter(Task.class, new TaskDeserializer())
                .registerTypeAdapter(Subtask.class, new SubtaskDeserializer(manager))
                .registerTypeAdapter(Epic.class, new EpicDeserializer(manager))
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        String query = exchange.getRequestURI().getQuery();
        String response = "";
        switch (exchange.getRequestMethod()) {
            case "POST":
                addOrUpdateTask(exchange, path);
                break;
            case "GET":
                if (path.length == 3) {
                    if (!path[2].equals("history")) {
                        response = getTask(TypeOfTasks.valueOf(path[2].toUpperCase()), query);
                        assert response != null;
                        sendResponseHeaders(response, exchange);
                    } else {
                        response = gson.toJson(manager.getHistoryList());
                        sendResponseHeaders(response, exchange);
                    }
                } else if (path.length == 2) {
                    response = gson.toJson(manager.getPrioritizedTasks());
                    sendResponseHeaders(response, exchange);
                }
                if (path.length == 4 && !query.isEmpty()) {
                    response = gson.toJson(manager.getSubtasksFromEpic(extractIdFromQuery(query)));
                    sendResponseHeaders(response, exchange);
                }
                break;
            case "DELETE":
                if (path.length == 3) {
                    deleteTask(TypeOfTasks.valueOf(path[2].toUpperCase()), query);
                    exchange.sendResponseHeaders(200, 0);
                    break;
                }
            default:
                exchange.sendResponseHeaders(405, 0);
        }
        try (
                OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }

    }

    private void sendResponseHeaders(String response, HttpExchange exchange) {
        try {
            if (response.equals("null") || response.equals("[]")) {
                exchange.sendResponseHeaders(404, 0);
            } else {
                exchange.sendResponseHeaders(200, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getTask(TypeOfTasks type, String query) {
        if (query != null) {
            int id = extractIdFromQuery(query);
            switch (type) {
                case TASK:
                    return gson.toJson(manager.getTask(id));
                case EPIC:
                    return gson.toJson(manager.getEpic(id));
                case SUBTASK:
                    return gson.toJson(manager.getSubtask(id));
            }
        } else {
            switch (type) {
                case TASK:
                    return gson.toJson(manager.getAllTasks());
                case EPIC:
                    return gson.toJson(manager.getAllEpics());
                case SUBTASK:
                    return gson.toJson(manager.getAllSubtasks());
            }
        }
        return null;
    }

    private void deleteTask(TypeOfTasks type, String query) {
        if (query != null) {
            int id = extractIdFromQuery(query);
            switch (type) {
                case TASK:
                    manager.deleteTask(id);
                    break;
                case EPIC:
                    manager.deleteEpic(id);
                    break;
                case SUBTASK:
                    manager.deleteSubtask(id);
                    break;
            }
        } else {
            switch (type) {
                case TASK:
                    manager.clearTasks();
                    break;
                case EPIC:
                    manager.clearEpics();
                    break;
                case SUBTASK:
                    manager.clearSubtasks();
                    break;
            }
        }
    }

    private Integer extractIdFromQuery(String query) {
        String[] split = query.split("=");
        return Integer.parseInt(split[1]);
    }

    private void addOrUpdateTask(HttpExchange exchange, String[] path) {
        JsonElement jsonElement = null;
        try {
            InputStream input = exchange.getRequestBody();
            jsonElement = JsonParser.parseString(new String(input.readAllBytes()));
            if (jsonElement.isJsonNull()) {
                exchange.sendResponseHeaders(400, 0);
            } else {
                switch (path[2]) {
                    case "task":
                        Task task = gson.fromJson(jsonElement, Task.class);
                        if (task.getId() != 0) {
                            manager.updateTask(task);
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            manager.addTask(task);
                            exchange.sendResponseHeaders(201, 0);
                        }
                        break;
                    case "subtask":
                        Subtask subtask = gson.fromJson(jsonElement, Subtask.class);
                        if (subtask.getId() != 0) {
                            manager.updateSubtask(subtask);
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            manager.addSubtask(subtask);
                            exchange.sendResponseHeaders(201, 0);
                        }
                        break;
                    case "epic":
                        Epic epic = gson.fromJson(jsonElement, Epic.class);
                        if (epic.getId() != 0) {
                            manager.updateEpic(epic);
                            exchange.sendResponseHeaders(200, 0);
                        } else {
                            manager.addEpic(epic);
                            exchange.sendResponseHeaders(201, 0);
                        }
                        break;
                    default:
                        exchange.sendResponseHeaders(400, 0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

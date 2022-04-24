package managers.http_backup;

import managers.FileBackedTasksManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;

    public HttpTaskManager(String address) {
        this.client = new KVTaskClient(address);
    }

    @Override
    protected void save() {
        String data = "";
        for (Task task : super.getAllTasks()) {
            data += task.toString(task) + '\n';
        }
        for (Epic epic : super.getAllEpics()) {
            data.trim();
            data += epic.toString(epic) + '\n';
        }
        for (Subtask subtask : super.getAllSubtasks()) {
            data.trim();
            data += subtask.toString(subtask) + '\n';
        }
        if (!super.getHistory().getHistory().isEmpty()) {
            data += '\n' + toString(super.getHistory());
        }
        if (!data.isEmpty()) {
            client.put("My_backup", data);
        } else {
            client.put("My_backup", " ");
        }
    }
}
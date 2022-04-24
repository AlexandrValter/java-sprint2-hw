package managers;

import managers.http_backup.HttpTaskManager;

public abstract class Managers {

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager<>();
    }

    public static TaskManager getDefault() {
        return new HttpTaskManager("http://localhost:");
    }

    public static TaskManager getDefault(String filename) {
        return new FileBackedTasksManager(filename);
    }
}
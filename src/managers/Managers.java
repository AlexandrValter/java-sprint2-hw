package managers;

public abstract class Managers {

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager<>();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistoryManager());
    }
}
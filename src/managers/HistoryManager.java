package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager {

    void addElement(Task task);

    List<Task> getHistory();
}
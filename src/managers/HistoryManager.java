package managers;

import tasks.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {

    void addElement(Task task);

    List<Task> getHistory();

    void remove(int id);

    LinkedListForTasks<Task> getTasksHistory();

    Map<Integer, Node<Task>> getTasksMap();

    void setTasksHistory(LinkedListForTasks<Task> tasksHistory);

    void setTasksMap(Map<Integer, Node<Task>> tasksMap);
}
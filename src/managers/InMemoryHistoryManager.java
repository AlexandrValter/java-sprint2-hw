package managers;

import tasks.Task;

import java.util.HashMap;
import java.util.LinkedList;

public class InMemoryHistoryManager<T> implements HistoryManager {
    private LinkedList<Task> tasksHistory = new LinkedList<>();

    @Override
    public void addElement(Task task) {
        if (tasksHistory.size() < 10) {
            tasksHistory.addLast(task);
        } else {
            tasksHistory.removeFirst();
            tasksHistory.addLast(task);
        }
    }

    @Override
    public LinkedList<Task> getHistory() {
        return tasksHistory;
    }

    @Override
    public String toString() {
        String output = "История просмотров: \n";
        for (Task item : tasksHistory) {
            output += "id=" + item.getId() + " '" + item.getName() + "' \n";
        }
        return output;
    }

    public void deleteElement(Task task) {
        while (tasksHistory.contains(task)) {
            tasksHistory.remove(task);
        }
    }

    public void deleteMultipleElements(HashMap<Integer, T> taskStorage) {
        for (T task : taskStorage.values()) {
            while (tasksHistory.contains(task)) {
                tasksHistory.remove(task);
            }
        }
    }
}
package managers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager<T> implements HistoryManager {
    private LinkedListForTasks<Task> tasksHistory = new LinkedListForTasks<>();
    private Map<Integer, Node<Task>> tasksMap = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        return tasksHistory.getTasks();
    }

    @Override
    public void addElement(Task task) {
        if (tasksMap.containsKey(task.getId())) {
            if (tasksHistory.getLast().data.getId() != task.getId()) {
                tasksHistory.removeNode(tasksMap.get(task.getId()));
                tasksHistory.linkLast(task);
                tasksMap.put(task.getId(), tasksHistory.getLast());
            }
        } else {
            tasksHistory.linkLast(task);
            tasksMap.put(task.getId(), tasksHistory.getLast());
        }
    }

    @Override
    public void remove(int id) {
        if (tasksMap.containsKey(id)) {
            tasksHistory.removeNode(tasksMap.get(id));
            tasksMap.remove(id);
        }
    }
}

class LinkedListForTasks<T extends Task> {
    private int size = 0;
    private Node<T> first;
    private Node<T> last;

    public LinkedListForTasks() {
    }

    public void linkLast(T task) {
        final Node<T> l = last;
        final Node<T> newNode = new Node<>(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    public List<T> getTasks() {
        List<T> tasks = new ArrayList<>();
        if (size != 0) {
            Node<T> node = first;
            for (int i = 1; i <= size; i++) {
                tasks.add(node.data);
                node = node.next;
            }
        }
        return tasks;
    }

    public void removeNode(Node<T> o) {
        Node<T> next = o.next;
        Node<T> prev = o.prev;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            o.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            o.next = null;
        }
        o.data = null;
        size--;
    }

    public Node<T> getLast() {
        return last;
    }
}
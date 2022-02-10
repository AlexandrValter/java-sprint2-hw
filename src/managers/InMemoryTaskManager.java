package managers;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskStorage = new HashMap<>();
    private final HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
    private Integer id = 1;
    private final InMemoryHistoryManager history;

    public InMemoryTaskManager(HistoryManager history) {
        this.history = (InMemoryHistoryManager) history;
    }

    public InMemoryHistoryManager getHistory() {
        return history;
    }

    @Override
    public void addTask(Task task) {
        task.setId(makeId());
        taskStorage.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(makeId());
        epicStorage.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtask.setId(makeId());
        subtaskStorage.put(subtask.getId(), subtask);
        epicStorage.get(subtask.getEpic().getId()).addSubtaskToEpic(subtask);
        subtask.getEpic().setStatus(getEpicStatus(subtask.getEpic().getId()));
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicStorage.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskStorage.values());
    }

    @Override
    public Task getTask(int id) {
        history.addElement(taskStorage.get(id));
        return taskStorage.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        this.id = id;
        history.addElement(epicStorage.get(id));
        return epicStorage.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        history.addElement(subtaskStorage.get(id));
        return subtaskStorage.get(id);
    }

    @Override
    public void clearTasks() {
        history.deleteMultipleElements(taskStorage);
        taskStorage.clear();
    }

    @Override
    public void clearEpics() {
        history.deleteMultipleElements(epicStorage);
        history.deleteMultipleElements(subtaskStorage);
        epicStorage.clear();
        subtaskStorage.clear();
    }

    @Override
    public void clearSubtasks() {
        history.deleteMultipleElements(subtaskStorage);
        subtaskStorage.clear();
        if (!epicStorage.isEmpty()) {
            for (int key : epicStorage.keySet()) {
                epicStorage.get(key).getSubtaskList().clear();
                epicStorage.get(key).setStatus(Statuses.NEW);
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        if (taskStorage.containsKey(task.getId())) {
            taskStorage.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicStorage.containsKey(epic.getId())) {
            epicStorage.put(epic.getId(), epic);
            epic.setStatus(getEpicStatus(epic.getId()));
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtaskStorage.containsKey(subtask.getId())) {
            subtaskStorage.put(subtask.getId(), subtask);
            Epic epic = subtask.getEpic();
            epic.deleteSubtaskFromEpic(subtask);
            epic.addSubtaskToEpic(subtask);
            subtask.getEpic().setStatus(getEpicStatus(subtask.getEpic().getId()));
        }
    }

    @Override
    public void deleteTask(int id) {
        history.deleteElement(taskStorage.get(id));
        taskStorage.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        history.deleteElement(epicStorage.get(id));
        epicStorage.remove(id);
        ArrayList<Integer> idSubtaskDelete = new ArrayList<>();
        for (Integer key : subtaskStorage.keySet()) {
            if (subtaskStorage.get(key).getEpic().getId() == id) {
                history.deleteElement(subtaskStorage.get(key));
                idSubtaskDelete.add(key);
            }
        }
        if (!idSubtaskDelete.isEmpty()) {
            for (Integer key : idSubtaskDelete) {
                subtaskStorage.remove(key);
            }
        }
    }

    @Override
    public void deleteSubtask(int id) {
        history.deleteElement(subtaskStorage.get(id));
        Epic epic = subtaskStorage.get(id).getEpic();
        epic.deleteSubtaskFromEpic(subtaskStorage.get(id));
        subtaskStorage.remove(id);
        epic.setStatus(getEpicStatus(epic.getId()));
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(int epicId) {
        return epicStorage.get(epicId).getSubtaskList();
    }

    private Statuses getEpicStatus(int id) {
        List<Statuses> statusSubtasks = new ArrayList<>();
        Statuses status = null;
        boolean amount = epicStorage.get(id).getSubtaskList().isEmpty();
        if (amount) {
            status = Statuses.NEW;
        } else {
            for (int i = 0; i < epicStorage.get(id).getSubtaskList().size(); i++) {
                statusSubtasks.add(epicStorage.get(id).getSubtaskList().get(i).getStatus());
            }
            if (statusSubtasks.contains(Statuses.NEW)) {
                status = Statuses.NEW;
            } else if (statusSubtasks.contains(Statuses.DONE) && !statusSubtasks.contains(Statuses.NEW)
                    && !statusSubtasks.contains(Statuses.IN_PROGRESS)) {
                status = Statuses.DONE;
            }
            if (statusSubtasks.contains(Statuses.IN_PROGRESS)) {
                status = Statuses.IN_PROGRESS;
            }
        }
        return status;
    }

    private int makeId() {
        return id++;
    }
}
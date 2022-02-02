package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private HashMap<Integer, Task> taskStorage = new HashMap<>();
    private HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
    private Integer id = 1;

    public void addTask(Task task) {
        task.setId(makeId());
        taskStorage.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(makeId());
        epicStorage.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(makeId());
        subtaskStorage.put(subtask.getId(), subtask);
        epicStorage.get(subtask.getEpic().getId()).addSubtaskToEpic(subtask);
        subtask.getEpic().setStatus(getEpicStatus(subtask.getEpic().getId()));
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskStorage.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicStorage.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskStorage.values());
    }

    public Task getTask(int id) {
        return taskStorage.get(id);
    }

    public Epic getEpic(int id) {
        return epicStorage.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtaskStorage.get(id);
    }

    public void clearTasks() {
        taskStorage.clear();
    }

    public void clearEpics() {
        epicStorage.clear();
        subtaskStorage.clear();
    }

    public void clearSubtasks() {
        subtaskStorage.clear();
        if (!epicStorage.isEmpty()) {
            for (int key : epicStorage.keySet()) {
                epicStorage.get(key).getSubtaskList().clear();
                epicStorage.get(key).setStatus("NEW");
            }
        }
    }

    public void updateTask(Task task) {
        if (taskStorage.containsKey(task.getId())) {
            taskStorage.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epicStorage.containsKey(epic.getId())) {
            epicStorage.put(epic.getId(), epic);
            epic.setStatus(getEpicStatus(epic.getId()));
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtaskStorage.containsKey(subtask.getId())) {
            subtaskStorage.put(subtask.getId(), subtask);
            Epic epic = subtask.getEpic();
            epic.deleteSubtaskFromEpic(subtask);
            epic.addSubtaskToEpic(subtask);
            subtask.getEpic().setStatus(getEpicStatus(subtask.getEpic().getId()));
        }
    }

    public void deleteTask(int id) {
        taskStorage.remove(id);
    }

    public void deleteEpic(int id) {
        epicStorage.remove(id);
        ArrayList<Integer> idSubtaskDelete = new ArrayList<>();
        for (Integer key : subtaskStorage.keySet()) {
            if (subtaskStorage.get(key).getEpic().getId() == id) {
                idSubtaskDelete.add(key);
            }
        }
        if (!idSubtaskDelete.isEmpty()) {
            for (Integer key : idSubtaskDelete) {
                subtaskStorage.remove(key);
            }
        }
    }

    public void deleteSubtask(int id) {
        Epic epic = subtaskStorage.get(id).getEpic();
        epic.deleteSubtaskFromEpic(subtaskStorage.get(id));
        subtaskStorage.remove(id);
        epic.setStatus(getEpicStatus(epic.getId()));
    }

    public ArrayList<Subtask> getSubtasksFromEpic(int epicId) {
        return epicStorage.get(epicId).getSubtaskList();
    }

    private String getEpicStatus(int id) {
        ArrayList<String> statusSubtasks = new ArrayList<>();
        String status = "ERROR";
        boolean amount = epicStorage.get(id).getSubtaskList().isEmpty();
        if (amount) {
            status = "NEW";
        } else {
            for (int i = 0; i < epicStorage.get(id).getSubtaskList().size(); i++) {
                statusSubtasks.add(epicStorage.get(id).getSubtaskList().get(i).getStatus());
            }
            if (statusSubtasks.contains("NEW")) {
                status = "NEW";
            } else if (statusSubtasks.contains("DONE") && !statusSubtasks.contains("NEW")
                    && !statusSubtasks.contains("IN_PROGRESS")) {
                status = "DONE";
            }
            if (statusSubtasks.contains("IN_PROGRESS")) {
                status = "IN_PROGRESS";
            }
        }
        return status;
    }

    private int makeId() {
        return id++;
    }
}
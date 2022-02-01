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
        addSubtaskToEpic(subtask);
        subtask.epic.setStatus(updateEpicStatus(subtask.epic.getId()));
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> listTasks = new ArrayList<>();
        for (Task task : taskStorage.values()) {
            listTasks.add(task);
        }
        return listTasks;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> listEpics = new ArrayList<>();
        for (Epic epic : epicStorage.values()) {
            listEpics.add(epic);
        }
        return listEpics;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> listSubtasks = new ArrayList<>();
        for (Subtask subtask : subtaskStorage.values()) {
            listSubtasks.add(subtask);
        }
        return listSubtasks;
    }

    public Task getTask(int id) {
        int neededId = 0;
        for (Integer i : taskStorage.keySet()) {
            if (i == id) {
                neededId = i;
            }
        }
        return taskStorage.get(neededId);
    }

    public Epic getEpic(int id) {
        int neededId = 0;
        for (Integer i : epicStorage.keySet()) {
            if (i == id) {
                neededId = i;
            }
        }
        return epicStorage.get(neededId);
    }

    public Subtask getSubtask(int id) {
        int neededId = 0;
        for (Integer i : subtaskStorage.keySet()) {
            if (i == id) {
                neededId = i;
            }
        }
        return subtaskStorage.get(neededId);
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
                epicStorage.get(key).status = updateEpicStatus(key);
            }
        }
    }

    public void updateTask(Task task) {
        for (Integer key : taskStorage.keySet()) {
            if (key == task.getId()) {
                taskStorage.put(key, task);
            }
        }
    }

    public void updateEpic(Epic epic) {
        for (Integer key : epicStorage.keySet()) {
            if (key == epic.getId()) {
                epicStorage.put(key, epic);
                epic.setStatus(updateEpicStatus(key));
            }
        }
    }

    public void updateSubtask(Subtask subtask) {
        for (Integer key : subtaskStorage.keySet()) {
            if (key == subtask.getId()) {
                subtaskStorage.put(key, subtask);
                for (int i = 0; i < epicStorage.get(subtask.epic.getId()).getSubtaskList().size(); i++) {
                    if (epicStorage.get(subtask.epic.getId()).getSubtaskList().get(i).id == subtask.id) {
                        epicStorage.get(subtask.epic.getId()).getSubtaskList().remove(i);
                        addSubtaskToEpic(subtask);
                    }
                }
                subtask.epic.setStatus(updateEpicStatus(subtask.epic.getId()));
            }
        }
    }

    public void deleteTask(int id) {
        boolean permission = false;
        for (Integer key : taskStorage.keySet()) {
            if (key == id) {
                permission = true;
            }
        }
        if (permission) {
            taskStorage.remove(id);
        }
    }

    public void deleteEpic(int id) {
        boolean permission = false;
        for (Integer key : epicStorage.keySet()) {
            if (key == id) {
                permission = true;
            }
        }
        if (permission) {
            epicStorage.remove(id);
            ArrayList<Integer> idSubtaskDelete = new ArrayList<>();
            for (Integer key : subtaskStorage.keySet()) {
                if (subtaskStorage.get(key).epic.getId() == id) {
                    idSubtaskDelete.add(key);
                }
            }
            if (!idSubtaskDelete.isEmpty()) {
                for (Integer key : idSubtaskDelete) {
                    subtaskStorage.remove(key);
                }
            }
        }
    }

    public void deleteSubtask(int id) {
        boolean permission = false;
        for (Integer key : subtaskStorage.keySet()) {
            if (key == id) {
                permission = true;
            }
        }
        if (permission) {
            int epicId = subtaskStorage.get(id).epic.getId();
            subtaskStorage.remove(id);
            for (int i = 0; i < epicStorage.get(epicId).getSubtaskList().size(); i++) {
                if (epicStorage.get(epicId).getSubtaskList().get(i).id == id) {
                    epicStorage.get(epicId).getSubtaskList().remove(i);
                }
            }
            epicStorage.get(epicId).setStatus(updateEpicStatus(epicId));
        }
    }

    public ArrayList<Subtask> getSubtasksFromEpic(int epicId) {
        return epicStorage.get(epicId).getSubtaskList();
    }

    public String updateEpicStatus(int id) {
        ArrayList<String> statusSubtasks = new ArrayList<>();
        String status = "ERROR";
        boolean amount = epicStorage.get(id).getSubtaskList().isEmpty();
        if (amount) {
            status = "NEW";
        } else {
            for (int i = 0; i < epicStorage.get(id).getSubtaskList().size(); i++) {
                statusSubtasks.add(epicStorage.get(id).getSubtaskList().get(i).status);
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

    public int makeId() {
        return id++;
    }

    public void addSubtaskToEpic(Subtask subtask) {
        epicStorage.get(subtask.epic.getId()).getSubtaskList().add(subtask);
    }
}
import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    static HashMap<Integer, Task> taskStorage = new HashMap<>();
    static HashMap<Integer, Epic> epicStorage = new HashMap<>();
    static HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
    static Integer id = 1;

    public static void addTask(Task task) {
        Task.setId(makeId(), task);
        taskStorage.put(Task.getId(task), task);
    }

    public static void addEpic(Epic epic) {
        Epic.setId(makeId(), epic);
        epicStorage.put(Epic.getId(epic), epic);
    }

    public static void addSubtask(Subtask subtask) {
        Subtask.setId(makeId(), subtask);
        subtaskStorage.put(Subtask.getId(subtask), subtask);
        addSubtaskToEpic(subtask.epicId, subtask);
        Epic.setStatus(updateEpicStatus(subtask.epicId), epicStorage.get(subtask.epicId));
    }

    public static ArrayList<Task> getAllTasks() {
        ArrayList<Task> listTasks = new ArrayList<>();
        for (Task task : taskStorage.values()) {
            listTasks.add(task);
        }
        return listTasks;
    }

    public static ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> listEpics = new ArrayList<>();
        for (Epic epic : epicStorage.values()) {
            listEpics.add(epic);
        }
        return listEpics;
    }

    public static ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> listSubtasks = new ArrayList<>();
        for (Subtask subtask : subtaskStorage.values()) {
            listSubtasks.add(subtask);
        }
        return listSubtasks;
    }

    public static Task getTask(int id) {
        int neededId = 0;
        for (Integer i : taskStorage.keySet()) {
            if (i == id) {
                neededId = i;
            }
        }
        return taskStorage.get(neededId);
    }

    public static Epic getEpic(int id) {
        int neededId = 0;
        for (Integer i : epicStorage.keySet()) {
            if (i == id) {
                neededId = i;
            }
        }
        return epicStorage.get(neededId);
    }

    public static Subtask getSubtask(int id) {
        int neededId = 0;
        for (Integer i : subtaskStorage.keySet()) {
            if (i == id) {
                neededId = i;
            }
        }
        return subtaskStorage.get(neededId);
    }

    public static void clearTasks() {
        taskStorage.clear();
    }

    public static void clearEpics() {
        epicStorage.clear();
        subtaskStorage.clear();
    }

    public static void clearSubtasks() {
        subtaskStorage.clear();
        if (!epicStorage.isEmpty()) {
            for (int key : epicStorage.keySet()) {
                epicStorage.get(key).getSubtaskList().clear();
                epicStorage.get(key).status = updateEpicStatus(key);
            }
        }
    }

    public static void updateTask(Task task) {
        for (Integer key : taskStorage.keySet()) {
            if (key == Task.getId(task)) {
                taskStorage.put(key, task);
            }
        }
    }

    public static void updateEpic(Epic epic) {
        for (Integer key : epicStorage.keySet()) {
            if (key == Epic.getId(epic)) {
                epicStorage.put(key, epic);
                Epic.setStatus(updateEpicStatus(key), epic);
            }
        }
    }

    public static void updateSubtask(Subtask subtask) {
        for (Integer key : subtaskStorage.keySet()) {
            if (key == Subtask.getId(subtask)) {
                subtaskStorage.put(key, subtask);
                for (int i = 0; i < epicStorage.get(subtask.epicId).getSubtaskList().size(); i++) {
                    if (epicStorage.get(subtask.epicId).getSubtaskList().get(i).id == subtask.id) {
                        epicStorage.get(subtask.epicId).getSubtaskList().remove(i);
                        addSubtaskToEpic(subtask.epicId, subtask);
                    }
                }
                Epic.setStatus(updateEpicStatus(subtask.epicId), epicStorage.get(subtask.epicId));
            }
        }
    }

    public static void deleteTask(int id) {
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

    public static void deleteEpic(int id) {
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
                if (subtaskStorage.get(key).epicId == id) {
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

    public static void deleteSubtask(int id) {
        boolean permission = false;
        for (Integer key : subtaskStorage.keySet()) {
            if (key == id) {
                permission = true;
            }
        }
        if (permission) {
            int epicId = subtaskStorage.get(id).epicId;
            subtaskStorage.remove(id);
            for (int i = 0; i < epicStorage.get(epicId).getSubtaskList().size(); i++) {
                if (epicStorage.get(epicId).getSubtaskList().get(i).id == id) {
                    epicStorage.get(epicId).getSubtaskList().remove(i);
                }
            }
            Epic.setStatus(updateEpicStatus(epicId), epicStorage.get(epicId));
        }
    }

    public static ArrayList<Subtask> getSubtasksFromEpic(int epicId) {
        return epicStorage.get(epicId).getSubtaskList();
    }

    public static String updateEpicStatus(int id) {
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

    public static int makeId() {
        return id++;
    }

    public static void addSubtaskToEpic(int id, Subtask subtask) {
        epicStorage.get(id).getSubtaskList().add(subtask);
    }
}
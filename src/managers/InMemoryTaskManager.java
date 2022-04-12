package managers;

import tasks.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskStorage = new HashMap<>();
    private final HashMap<Integer, Epic> epicStorage = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskStorage = new HashMap<>();
    private Integer id = 0;
    private final HistoryManager history = Managers.getDefaultHistoryManager();
    private final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);
    private final TreeSet<Task> sortedTasks = new TreeSet<>(comparator);

    @Override
    public List<Task> getHistoryList() {
        return history.getHistory();
    }

    @Override
    public void addTask(Task task) {
        task.setId(makeId());
        taskStorage.put(task.getId(), task);
        if (!addTaskPermission(task)) {
            throw new TaskValidationException("Задача '" + task.getName() +
                    "' пересекается по времени с другими задачами");
        } else {
            sortedTasks.add(task);
        }
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
        if (!addTaskPermission(subtask)) {
            throw new TaskValidationException("Подзадача '" + subtask.getName() +
                    "' пересекается по времени с другими задачами");
        } else {
            sortedTasks.add(subtask);
        }
        epicStorage.get(subtask.getEpic().getId()).addSubtaskToEpic(subtask);
        subtask.getEpic().setStatus(getEpicStatus(subtask.getEpic().getId()));
        subtask.getEpic().setStartTime();
        subtask.getEpic().setDuration();
        subtask.getEpic().setEndTime();
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
        if (taskStorage.containsKey(id)) {
            history.addElement(taskStorage.get(id));
            return taskStorage.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpic(int id) {
        if (epicStorage.containsKey(id)) {
            history.addElement(epicStorage.get(id));
            return epicStorage.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        if (subtaskStorage.containsKey(id)) {
            history.addElement(subtaskStorage.get(id));
            return subtaskStorage.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void clearTasks() {
        for (Task task : taskStorage.values()) {
            history.remove(task.getId());
        }
        taskStorage.clear();
    }

    @Override
    public void clearEpics() {
        for (Epic epic : epicStorage.values()) {
            history.remove(epic.getId());
        }
        for (Subtask subtask : subtaskStorage.values()) {
            history.remove(subtask.getId());
        }
        epicStorage.clear();
        subtaskStorage.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Subtask subtask : subtaskStorage.values()) {
            history.remove(subtask.getId());
        }
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
            Epic epic = subtask.getEpic();
            epic.deleteSubtaskFromEpic(subtaskStorage.get(subtask.getId()));
            epic.addSubtaskToEpic(subtask);
            subtaskStorage.put(subtask.getId(), subtask);
            subtask.getEpic().setStatus(getEpicStatus(subtask.getEpic().getId()));
        }
    }

    @Override
    public void deleteTask(int id) {
        history.remove(id);
        taskStorage.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        history.remove(id);
        epicStorage.remove(id);
        ArrayList<Integer> idSubtaskDelete = new ArrayList<>();
        for (Integer key : subtaskStorage.keySet()) {
            if (subtaskStorage.get(key).getEpic().getId() == id) {
                history.remove(key);
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
        if (subtaskStorage.containsKey(id)) {
            history.remove(id);
            Epic epic = subtaskStorage.get(id).getEpic();
            epic.deleteSubtaskFromEpic(subtaskStorage.get(id));
            subtaskStorage.remove(id);
            epic.setStatus(getEpicStatus(epic.getId()));
        }
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(int epicId) {
        if (epicStorage.containsKey(epicId)) {
            return epicStorage.get(epicId).getSubtaskList();
        }
        return null;
    }

    public HashMap<Integer, Task> getTaskStorage() {
        return taskStorage;
    }

    public HashMap<Integer, Epic> getEpicStorage() {
        return epicStorage;
    }

    public HashMap<Integer, Subtask> getSubtaskStorage() {
        return subtaskStorage;
    }

    public HistoryManager getHistory() {
        return history;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Set<Task> getPrioritizedTasks() {
        return sortedTasks;
    }

    private boolean addTaskPermission(Task task) {
        if (sortedTasks.isEmpty()) {
            return true;
        } else {
            Task lower = sortedTasks.lower(task);
            Task higher = sortedTasks.higher(task);
            if (lower == null) {
                return task.getEndTime().isBefore(sortedTasks.first().getStartTime());
            } else if (higher == null) {
                return task.getStartTime().isAfter(sortedTasks.last().getEndTime());
            }
            return lower.getEndTime().isBefore(task.getStartTime()) && higher.getStartTime().isAfter(task.getEndTime());
        }
    }

    private int makeId() {
        return ++id;
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
            } else if (statusSubtasks.contains(Statuses.NEW) && statusSubtasks.contains(Statuses.DONE)) {
                status = Statuses.IN_PROGRESS;
            }
        }
        return status;
    }
}
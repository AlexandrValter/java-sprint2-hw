package managers;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path fileBacked;

    public static void main(String[] args) throws IOException {
        FileBackedTasksManager manager = loadFromFile(Paths.get("backup.csv"));
        Task task1 = new Task("Задача №1", "Описание задачи №1");
        manager.addTask(task1);
        Task task2 = new Task("Задача №2", "Описание задачи №2");
        manager.addTask(task2);
        List<Subtask> subtaskList1 = new ArrayList<>();
        Epic epic1 = new Epic("Эпик №1", subtaskList1);
        manager.addEpic(epic1);
        List<Subtask> subtaskList2 = new ArrayList<>();
        Epic epic2 = new Epic("Эпик №2", subtaskList2);
        manager.addEpic(epic2);
        Subtask subtask1 = new Subtask("Подзадача №1", manager.getEpic(6));
        Subtask subtask2 = new Subtask("Подзадача №2", manager.getEpic(7));
        Subtask subtask3 = new Subtask("Подзадача №3", manager.getEpic(7));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
    }

    private FileBackedTasksManager(String fileName) {
        this.fileBacked = Paths.get(fileName);
    }

    @Override
    public List<Task> getHistoryList() {
        return super.getHistoryList();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public List<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return super.getAllSubtasks();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public List<Subtask> getSubtasksFromEpic(int epicId) {
        return super.getSubtasksFromEpic(epicId);
    }

    private void save() {
        try (Writer fileWriter = new FileWriter(String.valueOf(fileBacked.getFileName()), StandardCharsets.UTF_8)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            if (!super.getTaskStorage().isEmpty()) {
                for (Task task : super.getTaskStorage().values()) {
                    fileWriter.write(task.toString(task) + '\n');
                }
            }
            if (!super.getEpicStorage().isEmpty()) {
                for (Epic epic : super.getEpicStorage().values()) {
                    fileWriter.write(epic.toString(epic) + '\n');
                }
            }
            if (!super.getSubtaskStorage().isEmpty()) {
                for (Subtask subtask : super.getSubtaskStorage().values()) {
                    fileWriter.write(subtask.toString(subtask) + '\n');
                }
            }
            fileWriter.write('\n');
            if (super.getHistory().getTasksHistory().getFirst() != null) {
                fileWriter.write(toString(super.getHistory()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    static String toString(HistoryManager manager) {
        String result = "";
        if (manager.getTasksHistory().getFirst() != null) {
            Node<Task> task = manager.getTasksHistory().getFirst();
            while (task.next != null) {
                result += task.data.getId() + ",";
                task = task.next;
            }
            result += task.data.getId();
        }
        return result;
    }

    static List<Integer> fromString(String[] value) {
        List<Integer> history = new ArrayList<>();
        for (String item : value) {
            history.add(Integer.valueOf(item));
        }
        return history;
    }

    private List<String> fromFileToString(Path path) throws IOException {
        List<String> list;
        try (Stream<String> lines = Files.lines(path)) {
            list = lines.collect(Collectors.toList());
        }
        return list;
    }

    private void taskAllocation(String[] value, FileBackedTasksManager manager) {
        if (TypeOfTasks.valueOf(value[1]).equals(TypeOfTasks.TASK)) {
            manager.getTaskStorage().put(Integer.valueOf(value[0]), new Task().fromString(value));
        } else if (TypeOfTasks.valueOf(value[1]).equals(TypeOfTasks.EPIC)) {
            manager.getEpicStorage().put(Integer.valueOf(value[0]), new Epic().fromString(value));
        } else if (TypeOfTasks.valueOf(value[1]).equals(TypeOfTasks.SUBTASK)) {
            Subtask subtask = new Subtask().fromString(value);
            manager.getSubtaskStorage().put(Integer.valueOf(value[0]), subtask);
            subtask.setEpic(manager.getEpicStorage().get(Integer.valueOf(value[5])));
            if (subtask.getEpic().getSubtaskList() == null) {
                subtask.getEpic().setSubtaskList(new ArrayList<>());
                subtask.getEpic().getSubtaskList().add(subtask);
            } else {
                subtask.getEpic().getSubtaskList().add(subtask);
            }
        }
    }

    private void historyAllocation(List<Integer> history, FileBackedTasksManager manager) {
        LinkedListForTasks<Task> tasksHistory = new LinkedListForTasks<>();
        Map<Integer, Node<Task>> tasksMap = new HashMap<>();
        for (Integer id : history) {
            if (super.getTaskStorage().containsKey(id)) {
                tasksHistory.linkLast(manager.getTaskStorage().get(id));
                tasksMap.put(id, tasksHistory.getLast());
            } else if (super.getEpicStorage().containsKey(id)) {
                tasksHistory.linkLast(manager.getEpicStorage().get(id));
                tasksMap.put(id, tasksHistory.getLast());
            } else {
                tasksHistory.linkLast(manager.getSubtaskStorage().get(id));
                tasksMap.put(id, tasksHistory.getLast());
            }
        }
        manager.getHistory().setTasksMap(tasksMap);
        manager.getHistory().setTasksHistory(tasksHistory);
    }

    private static FileBackedTasksManager loadFromFile(Path path) throws IOException {
        FileBackedTasksManager manager = new FileBackedTasksManager(String.valueOf(path.getFileName()));
        List<String> data = new ArrayList<>(manager.fromFileToString(path));
        int currentId = 0;
        if (!data.isEmpty()) {
            for (int i = 1; i < data.size(); i++) {
                String[] split = data.get(i).split(",");
                if (split.length == 1) {
                    manager.historyAllocation(fromString(data.get(i + 1).split(",")), manager);
                    break;
                } else {
                    while (true) {
                        manager.taskAllocation(split, manager);
                        if (currentId < Integer.valueOf(split[0])) {
                            currentId = Integer.valueOf(split[0]);
                        }
                        break;
                    }
                }
            }
            manager.setId(currentId);
        }
        return manager;
    }
}
package managers;

import tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private Path fileBacked;

    public FileBackedTasksManager() {
    }

    public FileBackedTasksManager(String fileName) {
        this.fileBacked = Paths.get(fileName);
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

    protected void save() {
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
            if (!super.getHistory().getHistory().isEmpty()) {
                fileWriter.write(toString(super.getHistory()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    protected static FileBackedTasksManager loadFromFile(Path path) {
        FileBackedTasksManager manager = (FileBackedTasksManager) Managers.getDefault(String.valueOf(path.getFileName()));
        List<String> data = new ArrayList<>(manager.fromFileToString(path));
        int currentId = 0;
        if (!data.isEmpty()) {
            for (int i = 1; i < data.size(); i++) {
                String[] split = data.get(i).split(",");
                if ((data.get(i - 1).split(",")[0].equals("")) && (!split[0].isBlank())) {
                    manager.historyAllocation(fromString(split), manager);
                    break;
                } else {
                    while (true) {
                        manager.taskAllocation(split, manager);
                        if (!split[0].isBlank() && currentId < Integer.valueOf(split[0])) {
                            currentId = Integer.valueOf(split[0]);
                        }
                        break;
                    }
                }
            }
            manager.setId(currentId);
        }
        for (Task task : manager.getTaskStorage().values()) {
            manager.getPrioritizedTasks().add(task);
        }
        for (Subtask subtask : manager.getSubtaskStorage().values()) {
            manager.getPrioritizedTasks().add(subtask);
        }
        return manager;
    }

    protected static String toString(HistoryManager manager) {
        String result = "";
        for (int i = 0; i < (manager.getHistory().size() - 1); i++) {
            result += manager.getHistory().get(i).getId() + ",";
        }
        return result + manager.getHistory().get(manager.getHistory().size() - 1).getId();
    }

    private static List<Integer> fromString(String[] value) {
        List<Integer> history = new ArrayList<>();
        for (String item : value) {
            history.add(Integer.valueOf(item));
        }
        return history;
    }

    private List<String> fromFileToString(Path path) {
        List<String> list;
        try (Stream<String> lines = Files.lines(path)) {
            list = lines.collect(Collectors.toList());
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return list;
    }

    private void taskAllocation(String[] value, FileBackedTasksManager manager) {
        if (value.length > 4) {
            if (TypeOfTasks.valueOf(value[1]).equals(TypeOfTasks.TASK)) {
                manager.getTaskStorage().put(Integer.valueOf(value[0]), new Task().fromString(value));
            } else if (TypeOfTasks.valueOf(value[1]).equals(TypeOfTasks.EPIC)) {
                manager.getEpicStorage().put(Integer.valueOf(value[0]), new Epic().fromString(value));
            } else if (TypeOfTasks.valueOf(value[1]).equals(TypeOfTasks.SUBTASK)) {
                Subtask subtask = new Subtask().fromString(value);
                manager.getSubtaskStorage().put(Integer.valueOf(value[0]), subtask);
                subtask.setEpic(manager.getEpicStorage().get(Integer.valueOf(value[7])));
                subtask.getEpic().getSubtaskList().add(subtask);
                subtask.getEpic().setStartTime();
                subtask.getEpic().setDuration();
                subtask.getEpic().setEndTime();
            }
        }
    }

    private void historyAllocation(List<Integer> history, FileBackedTasksManager manager) {
        for (Integer id : history) {
            Task task = null;
            if (super.getTaskStorage().containsKey(id)) {
                task = manager.getTaskStorage().get(id);
            } else if (super.getEpicStorage().containsKey(id)) {
                task = manager.getEpicStorage().get(id);
            } else {
                task = manager.getSubtaskStorage().get(id);
            }
            manager.getHistory().addElement(task);
        }
    }
}
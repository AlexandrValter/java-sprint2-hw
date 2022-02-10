import managers.*;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefault();
        InMemoryHistoryManager history = manager.getHistory();
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
        Subtask subtask1 = new Subtask("Подзадача №1", manager.getEpic(3));
        Subtask subtask2 = new Subtask("Подзадача №2", manager.getEpic(3));
        Subtask subtask3 = new Subtask("Подзадача №3", manager.getEpic(4));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        subtask3.setStatus(Statuses.DONE);
        subtask1.setStatus(Statuses.IN_PROGRESS);
        task1.setStatus(Statuses.IN_PROGRESS);
        task2.setStatus(Statuses.DONE);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask3);
        manager.updateTask(task1);
        manager.updateTask(task2);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.print(history);
        manager.deleteTask(1);
        manager.deleteEpic(3);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
        System.out.print(history);
        manager.getTask(2);
        manager.getEpic(4);
        manager.getTask(2);
        manager.getSubtask(6);
        manager.getEpic(4);
        manager.getTask(2);
        manager.getSubtask(6);
        manager.getTask(2);
        manager.getSubtask(6);
        manager.getEpic(4);
        System.out.print(history);
        manager.deleteEpic(4);
        System.out.print(history);
    }
}
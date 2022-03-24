import managers.*;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
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
        Subtask subtask3 = new Subtask("Подзадача №3", manager.getEpic(3));
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        printList(manager.getHistoryList());
        manager.getTask(2);
        manager.getEpic(4);
        manager.getTask(1);
        manager.getSubtask(5);
        manager.getTask(2);
        manager.getSubtask(5);
        manager.getSubtask(5);
        manager.getEpic(3);
        manager.getSubtask(7);
        manager.getEpic(4);
        printList(manager.getHistoryList());
        manager.deleteTask(2);
        printList(manager.getHistoryList());
        manager.deleteEpic(3);
        printList(manager.getHistoryList());
    }

    //метод для вывода истории просмотров задач при тестах
    public static void printList(List<Task> list) {
        System.out.println("История просмотров:");
        for (Task task : list) {
            System.out.println(task);
        }
    }
}
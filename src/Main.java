import managers.*;

import tasks.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager manager = (FileBackedTasksManager) Managers.getDefault("backup.csv");
        Task task1 = new Task("Задача №1", "Описание задачи №1", "11.01.2022 09:00", 120);
        manager.addTask(task1);
        Task task2 = new Task("Задача №2", "Описание задачи №2", "10.01.2022 21:00", 45);
        manager.addTask(task2);
        Task task3 = new Task("Задача №3", "Описание задачи №3", "08.02.2022 15:22", 93);
        manager.addTask(task3);
        Epic epic1 = new Epic("Эпик №1", new ArrayList<>());
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик №2", new ArrayList<>());
        manager.addEpic(epic2);
        Subtask subtask1 = new Subtask("NameSubtask1", epic1, "05.04.2022 15:35", 28);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("NameSubtask2", epic1, "03.03.2021 15:32", 15);
        manager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask("NameSubtask3", epic1, "01.01.2021 00:00", 127);
        manager.addSubtask(subtask3);
        printList(manager.getHistoryList());
        manager.getTask(2);
        manager.getEpic(5);
        manager.getTask(3);
        manager.getSubtask(7);
        manager.getTask(1);
        manager.getSubtask(8);
        manager.getSubtask(6);
        manager.getEpic(5);
        manager.getSubtask(7);
        manager.getEpic(4);
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
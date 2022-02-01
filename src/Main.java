import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Задача №1", "Описание задачи №1");
        manager.addTask(task1);
        Task task2 = new Task("Задача №2", "Описание задачи №2");
        manager.addTask(task2);
        ArrayList<Subtask> subtaskList1 = new ArrayList<>();
        Epic epic1 = new Epic("Эпик №1", subtaskList1);
        manager.addEpic(epic1);
        ArrayList<Subtask> subtaskList2 = new ArrayList<>();
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
        printTask(manager, 1);
        printTask(manager, 2);
        printEpic(manager, 3);
        printEpic(manager, 4);
        printSubtask(manager, 5);
        printSubtask(manager, 6);
        printSubtask(manager, 7);
        subtask3.status = "DONE";
        subtask1.status = "IN_PROGRESS";
        task1.status = "IN_PROGRESS";
        task2.status = "DONE";
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask3);
        manager.updateTask(task1);
        manager.updateTask(task2);
        printTask(manager, 1);
        printTask(manager, 2);
        printEpic(manager, 3);
        printEpic(manager, 4);
        printSubtask(manager, 5);
        printSubtask(manager, 6);
        printSubtask(manager, 7);
        manager.deleteTask(1);
        manager.deleteEpic(3);
        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubtasks());
    }

    public static void printEpic(Manager manager, int id) {
        System.out.println(manager.getEpic(id).name + " " + manager.getEpic(id).status);
    }

    public static void printTask(Manager manager, int id) {
        System.out.println(manager.getTask(id).name + " " + manager.getTask(id).status);
    }

    public static void printSubtask(Manager manager, int id) {
        System.out.println(manager.getSubtask(id).name + " " + manager.getSubtask(id).status);
    }
}


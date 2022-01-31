import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("Задача №1", "Описание задачи №1");
        Manager.addTask(task1);
        Task task2 = new Task("Задача №2", "Описание задачи №2");
        Manager.addTask(task2);
        ArrayList<Subtask> subtaskList1 = new ArrayList<>();
        Epic epic1 = new Epic("Эпик №1", subtaskList1);
        Manager.addEpic(epic1);
        ArrayList<Subtask> subtaskList2 = new ArrayList<>();
        Epic epic2 = new Epic("Эпик №2", subtaskList2);
        Manager.addEpic(epic2);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1.id);
        Subtask subtask2 = new Subtask("Подзадача №2", epic1.id);
        Subtask subtask3 = new Subtask("Подзадача №3", epic2.id);
        Manager.addSubtask(subtask1);
        Manager.addSubtask(subtask2);
        Manager.addSubtask(subtask3);
        System.out.println(Manager.getAllTasks());
        System.out.println(Manager.getAllEpics());
        System.out.println(Manager.getAllSubtasks());
        printTask(1);
        printTask(2);
        printEpic(3);
        printEpic(4);
        printSubtask(5);
        printSubtask(6);
        printSubtask(7);
        subtask3.status = "DONE";
        subtask1.status = "IN_PROGRESS";
        task1.status = "IN_PROGRESS";
        task2.status = "DONE";
        Manager.updateSubtask(subtask1);
        Manager.updateSubtask(subtask3);
        Manager.updateTask(task1);
        Manager.updateTask(task2);
        printTask(1);
        printTask(2);
        printEpic(3);
        printEpic(4);
        printSubtask(5);
        printSubtask(6);
        printSubtask(7);
        Manager.deleteTask(1);
        Manager.deleteEpic(3);
        System.out.println(Manager.getAllTasks());
        System.out.println(Manager.getAllEpics());
        System.out.println(Manager.getAllSubtasks());
    }

    public static void printEpic(int id) {
        System.out.println(Manager.getEpic(id).name + " " + Manager.getEpic(id).status);
    }

    public static void printTask(int id) {
        System.out.println(Manager.getTask(id).name + " " + Manager.getTask(id).status);
    }

    public static void printSubtask(int id) {
        System.out.println(Manager.getSubtask(id).name + " " + Manager.getSubtask(id).status);
    }
}


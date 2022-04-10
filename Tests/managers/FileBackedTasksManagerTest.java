package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    private final Task task1 = new Task(
            "Задача №1",
            "Задача №1 для тестов",
            "11.04.2022 09:00",
            120
    );
    private final Task task2 = new Task(
            "Задача №2",
            "Задача №2 для тестов",
            "27.07.2021 11:35",
            159
    );
    private final Epic epic1 = new Epic("Эпик №1", new ArrayList<>());
    private final Epic epic2 = new Epic("Эпик №2", new ArrayList<>());

    @BeforeEach
    @Override
    public void createManager() {
        manager = new FileBackedTasksManager("FileBackedTasksManagerTest.csv");
    }

    @Test
    public void test1_shouldSaveAndRecoveryEmptyTasksList() {
        manager = new FileBackedTasksManager("test1.csv");
        assertTrue(manager.getHistory().getHistory().isEmpty());
        assertTrue(manager.getTaskStorage().isEmpty());
        assertTrue(manager.getEpicStorage().isEmpty());
        assertTrue(manager.getSubtaskStorage().isEmpty());
        manager.save();
        assertTrue(manager.getHistory().getHistory().isEmpty());
        assertTrue(manager.getTaskStorage().isEmpty());
        assertTrue(manager.getEpicStorage().isEmpty());
        assertTrue(manager.getSubtaskStorage().isEmpty());
        FileBackedTasksManager.loadFromFile(Path.of("test1.csv"));
        assertTrue(manager.getHistory().getHistory().isEmpty());
        assertTrue(manager.getTaskStorage().isEmpty());
        assertTrue(manager.getEpicStorage().isEmpty());
        assertTrue(manager.getSubtaskStorage().isEmpty());
    }

    @Test
    public void test2_shouldSaveAndRecoveryEpicAndTasks() {
        manager = new FileBackedTasksManager("test2.csv");
        manager.save();
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic2);
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(Path.of("test2.csv"));
        assertTrue(restoredManager.getHistory().getHistory().isEmpty());
        assertEquals(task1.toString(), restoredManager.getTask(1).toString());
        assertEquals(task2.toString(), restoredManager.getTask(2).toString());
        assertEquals(epic2.toString(), restoredManager.getEpic(3).toString());
    }

    @Test
    public void test3_shouldSaveAndRecoveryTasksAndHistory() {
        manager = new FileBackedTasksManager("test3.csv");
        manager.save();
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача №2", epic1, "16.12.2021 14:18", 189);
        manager.addSubtask(subtask2);
        manager.getEpic(3);
        manager.getTask(2);
        manager.getSubtask(5);
        manager.getSubtask(4);
        manager.getTask(1);
        FileBackedTasksManager restoredManager = FileBackedTasksManager.loadFromFile(Path.of("test3.csv"));
        assertEquals(manager.getHistory().getHistory().toString(), restoredManager.getHistory().getHistory().toString());
        assertEquals(manager.getAllTasks().toString(), restoredManager.getAllTasks().toString());
        assertEquals(manager.getAllEpics().toString(), restoredManager.getAllEpics().toString());
        assertEquals(manager.getAllSubtasks().toString(), restoredManager.getAllSubtasks().toString());
    }
}

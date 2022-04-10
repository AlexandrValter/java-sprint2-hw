package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T manager;
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
    private final Task task3 = new Task(
            "Задача №3",
            "Задача №3 для тестов",
            "20.09.2021 19:20",
            425
    );
    private final Epic epic1 = new Epic("Эпик №1", new ArrayList<>());
    private final Epic epic2 = new Epic("Эпик №2", new ArrayList<>());
    private final Epic epic3 = new Epic("Эпик №3", new ArrayList<>());

    @BeforeEach
    public abstract void createManager();

    @Test
    public void test1_shouldAddNewTask() {
        assertTrue(manager.getAllTasks().isEmpty());
        manager.addTask(task1);
        assertEquals(task1, manager.getTask(1));
    }

    @Test
    public void test2_shouldAddNewEpic() {
        assertTrue(manager.getAllEpics().isEmpty());
        manager.addEpic(epic1);
        assertEquals(epic1, manager.getEpic(1));
        assertTrue(manager.getSubtasksFromEpic(1).isEmpty());
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertTrue(manager.getSubtasksFromEpic(1).contains(subtask1));
    }

    @Test
    public void test3_shouldAddNewSubtask() {
        assertTrue(manager.getAllSubtasks().isEmpty());
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(subtask1, manager.getSubtask(2));
        assertEquals(epic1, manager.getSubtask(2).getEpic());
    }

    @Test
    public void test4_shouldReturnAllTasks() {
        assertEquals(List.of(), manager.getAllTasks());
        manager.addTask(task1);
        manager.addTask(task2);
        assertEquals(List.of(task1, task2), manager.getAllTasks());
        assertNotEquals(List.of(task1, task2, task3), manager.getAllTasks());
    }

    @Test
    public void test5_shouldReturnAllEpics() {
        assertEquals(List.of(), manager.getAllEpics());
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        assertEquals(List.of(epic1, epic2), manager.getAllEpics());
        assertNotEquals(List.of(epic1, epic2, epic3), manager.getAllEpics());
    }

    @Test
    public void test6_shouldReturnAllSubtasks() {
        assertEquals(List.of(), manager.getAllSubtasks());
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача №2", epic1, "16.12.2021 14:18", 189);
        manager.addSubtask(subtask2);
        assertEquals(List.of(subtask1, subtask2), manager.getAllSubtasks());
        Subtask subtask3 = new Subtask("Подзадача №3", epic1, "21.06.2021 15:42", 63);
        assertNotEquals(List.of(subtask1, subtask2, subtask3), manager.getAllSubtasks());
    }

    @Test
    public void test7_shouldReturnTaskFromId() {
        assertNull(manager.getTask(1));
        manager.addTask(task1);
        assertEquals(task1, manager.getTask(1));
    }

    @Test
    public void test8_shouldReturnEpicAndSubtaskFromId() {
        assertNull(manager.getEpic(1));
        assertNull(manager.getSubtask(2));
        manager.addEpic(epic1);
        assertEquals(epic1, manager.getEpic(1));
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(subtask1, manager.getSubtask(2));
    }

    @Test
    public void test9_shouldClearTasks() {
        assertTrue(manager.getAllTasks().isEmpty());
        manager.clearTasks();
        assertTrue(manager.getAllTasks().isEmpty());
        manager.addTask(task1);
        assertEquals(1, manager.getAllTasks().size());
        manager.clearTasks();
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void test10_shouldClearEpics() {
        assertTrue(manager.getAllEpics().isEmpty());
        manager.clearEpics();
        assertTrue(manager.getAllEpics().isEmpty());
        manager.addEpic(epic1);
        assertEquals(1, manager.getAllEpics().size());
        manager.clearEpics();
        assertTrue(manager.getAllEpics().isEmpty());
        manager.addEpic(epic2);
        assertEquals(1, manager.getAllEpics().size());
        assertTrue(manager.getAllSubtasks().isEmpty());
        Subtask subtask1 = new Subtask("Подзадача №1", epic2, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(1, manager.getAllSubtasks().size());
        manager.clearEpics();
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void test11_shouldClearSubtasks() {
        assertTrue(manager.getAllSubtasks().isEmpty());
        manager.clearSubtasks();
        assertTrue(manager.getAllSubtasks().isEmpty());
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(1, manager.getAllSubtasks().size());
        manager.clearSubtasks();
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void test12_shouldUpdateEpicStatusAfterClearSubtasks() {
        manager.addEpic(epic1);
        assertEquals(Statuses.NEW, epic1.getStatus());
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача №2", epic1, "16.12.2021 14:18", 189);
        manager.addSubtask(subtask2);
        subtask2.setStatus(Statuses.IN_PROGRESS);
        manager.updateSubtask(subtask2);
        assertEquals(Statuses.IN_PROGRESS, epic1.getStatus());
        manager.clearSubtasks();
        assertEquals(Statuses.NEW, epic1.getStatus());
    }

    @Test
    public void test13_shouldDeleteTask() {
        assertTrue(manager.getAllTasks().isEmpty());
        manager.deleteTask(1);
        assertTrue(manager.getAllTasks().isEmpty());
        manager.addTask(task1);
        assertEquals(1, manager.getAllTasks().size());
        manager.deleteTask(2);
        assertEquals(1, manager.getAllTasks().size());
        manager.deleteTask(1);
        assertTrue(manager.getAllTasks().isEmpty());
    }

    @Test
    public void test14_shouldDeleteEpic() {
        assertTrue(manager.getAllEpics().isEmpty());
        manager.deleteEpic(1);
        assertTrue(manager.getAllEpics().isEmpty());
        manager.addEpic(epic1);
        assertEquals(1, manager.getAllEpics().size());
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(1, manager.getAllSubtasks().size());
        manager.deleteEpic(2);
        assertEquals(1, manager.getAllEpics().size());
        manager.deleteEpic(1);
        assertTrue(manager.getAllEpics().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
    }

    @Test
    public void test15_shouldDeleteSubtaskAndUpdateEpicStatusAfterDeleteSubtask() {
        assertTrue(manager.getAllSubtasks().isEmpty());
        manager.deleteSubtask(1);
        assertTrue(manager.getAllSubtasks().isEmpty());
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(1, manager.getAllSubtasks().size());
        manager.deleteSubtask(1);
        assertEquals(1, manager.getAllSubtasks().size());
        subtask1.setStatus(Statuses.DONE);
        manager.updateSubtask(subtask1);
        assertEquals(Statuses.DONE, manager.getEpic(1).getStatus());
        manager.deleteSubtask(2);
        assertTrue(manager.getAllSubtasks().isEmpty());
        assertEquals(Statuses.NEW, manager.getEpic(1).getStatus());
    }

    @Test
    public void test16_shouldUpdateTask() {
        manager.addTask(task1);
        assertEquals(task1.getName(), manager.getTask(1).getName());
        Task updatedTask = new Task(
                1,
                "Name2",
                "Description2",
                Statuses.IN_PROGRESS,
                TypeOfTasks.TASK,
                LocalDateTime.of(2021, 11, 7, 6, 33),
                Duration.ofMinutes(88)
        );
        assertNotEquals("Name2", manager.getTask(1).getName());
        manager.updateTask(updatedTask);
        assertEquals("Name2", manager.getTask(1).getName());
    }

    @Test
    public void test17_shouldUpdateEpic() {
        manager.addEpic(epic1);
        assertEquals(epic1.getName(), manager.getEpic(1).getName());
        Epic updatedEpic = new Epic(
                1,
                "Name2",
                "Description2",
                Statuses.IN_PROGRESS,
                TypeOfTasks.EPIC,
                LocalDateTime.of(2021, 11, 7, 6, 33),
                Duration.ofMinutes(88)
        );
        updatedEpic.setSubtaskList(new ArrayList<>());
        assertNotEquals("Name2", manager.getEpic(1).getName());
        manager.updateEpic(updatedEpic);
        assertEquals("Name2", manager.getEpic(1).getName());
    }

    @Test
    public void test18_shouldUpdateSubtask() {
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(subtask1.getName(), manager.getSubtask(2).getName());
        Subtask updatedSubtask = new Subtask(
                2,
                "Name2",
                "Description2",
                Statuses.IN_PROGRESS,
                TypeOfTasks.SUBTASK,
                LocalDateTime.of(2021, 11, 7, 6, 33),
                Duration.ofMinutes(88)
        );
        updatedSubtask.setEpic(epic1);
        assertNotEquals("Name2", manager.getSubtask(2).getName());
        manager.updateSubtask(updatedSubtask);
        assertEquals("Name2", manager.getSubtask(2).getName());
    }

    @Test
    public void test19_shouldUpdateEpicStatusAfterUpdateSubtask() {
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(Statuses.NEW, manager.getEpic(1).getStatus());
        subtask1.setStatus(Statuses.DONE);
        assertNotEquals(Statuses.DONE, manager.getEpic(1).getStatus());
        manager.updateSubtask(subtask1);
        assertEquals(Statuses.DONE, manager.getEpic(1).getStatus());
    }

    @Test
    public void test20_shouldUpdateSubtaskListFromEpicAfterUpdateSubtask() {
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(epic1.getSubtaskList(), manager.getEpic(1).getSubtaskList());
        Subtask updatedSubtask = new Subtask(
                2,
                "Name2",
                "Description2",
                Statuses.IN_PROGRESS,
                TypeOfTasks.SUBTASK,
                LocalDateTime.of(2021, 11, 7, 6, 33),
                Duration.ofMinutes(88)
        );
        updatedSubtask.setEpic(epic1);
        assertNotEquals(List.of(updatedSubtask), manager.getEpic(1).getSubtaskList());
        manager.updateSubtask(updatedSubtask);
        assertEquals(List.of(updatedSubtask), manager.getEpic(1).getSubtaskList());
    }

    @Test
    public void test21_shouldReturnSubtasksFromEpic() {
        assertTrue(manager.getAllEpics().isEmpty());
        manager.getSubtasksFromEpic(1);
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertFalse(manager.getAllEpics().isEmpty());
        assertEquals(List.of(subtask1), manager.getSubtasksFromEpic(1));
        assertNotEquals(List.of(subtask1), manager.getSubtasksFromEpic(3));
    }

    @Test
    public void test22_shouldReturnHistoryList() {
        assertTrue(manager.getHistoryList().isEmpty());
        manager.addTask(task1);
        manager.getTask(1);
        assertEquals(List.of(task1), manager.getHistoryList());
    }

    @Test
    public void test23_shouldReturnPrioritizedList() {
        assertTrue(manager.getAllTasks().isEmpty());
        assertTrue(manager.getAllSubtasks().isEmpty());
        assertTrue(manager.getPrioritizedTasks().isEmpty());
        manager.addTask(task1);
        manager.addTask(task3);
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96));
        assertEquals(3, manager.getPrioritizedTasks().size());
    }

    @Test
    public void test24_shouldReturnTreeSetSortedByStartTime() {
        manager.addTask(task3);
        manager.addTask(task2);
        manager.addTask(task1);
        assertEquals(List.of(task2, task3, task1).toString(), manager.getPrioritizedTasks().toString());
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        Subtask subtask2 = new Subtask("Подзадача №2", epic1, "22.11.2015 20:14", 135);
        Subtask subtask3 = new Subtask("Подзадача №3", epic1, "31.10.2022 18:00", 60);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        assertEquals(6, manager.getPrioritizedTasks().size());
        assertEquals(List.of(subtask2, task2, subtask1, task3, task1, subtask3).toString(),
                manager.getPrioritizedTasks().toString());
    }

    @Test
    public void test25_shouldNodAddTasksInTreeSet() {
        manager.addTask(task1);
        Task testTask = task1;
        assertEquals(1, manager.getPrioritizedTasks().size());
        manager.addTask(testTask);
        assertEquals(1, manager.getPrioritizedTasks().size());
        manager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Подзадача №1", epic1, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals(2, manager.getPrioritizedTasks().size());
        Subtask testSubtask = subtask1;
        manager.addSubtask(testSubtask);
        assertEquals(2, manager.getPrioritizedTasks().size());
    }
}
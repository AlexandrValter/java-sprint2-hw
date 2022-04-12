package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private HistoryManager manager;
    private final Task task1 = new Task(
            1,
            "Name1",
            "Description1",
            Statuses.NEW,
            TypeOfTasks.TASK,
            LocalDateTime.of(2021, 11, 7, 6, 33),
            Duration.ofMinutes(88)
    );
    private final Task task2 = new Task(
            2,
            "Name2",
            "Description2",
            Statuses.NEW,
            TypeOfTasks.TASK,
            LocalDateTime.of(2022, 1, 27, 13, 55),
            Duration.ofMinutes(113)
    );
    private final Task task3 = new Task(
            5,
            "Name5",
            "Description5",
            Statuses.NEW,
            TypeOfTasks.TASK,
            LocalDateTime.of(2022, 4, 11, 18, 17),
            Duration.ofMinutes(243)
    );
    private final Subtask subtask = new Subtask(
            3,
            "Name3",
            "Description3",
            Statuses.NEW,
            TypeOfTasks.SUBTASK,
            LocalDateTime.of(2021, 10, 10, 10, 10),
            Duration.ofMinutes(101)
    );

    @BeforeEach
    public void createManager() {
        manager = Managers.getDefaultHistoryManager();
    }

    @Test
    public void test1_shouldAddElementInEmptyHistory() {
        assertTrue(manager.getHistory().isEmpty());
        manager.addElement(subtask);
        manager.addElement(task1);
        Epic epic = new Epic("Name4", new ArrayList<>());
        epic.setId(4);
        manager.addElement(epic);
        assertEquals(List.of(subtask, task1, epic), manager.getHistory());
    }

    @Test
    public void test2_shouldRemoveElementFromHistory() {
        assertTrue(manager.getHistory().isEmpty());
        manager.remove(1);
        Epic epic = new Epic("Name4", new ArrayList<>());
        epic.setId(4);
        manager.addElement(epic);
        manager.addElement(task1);
        assertEquals(List.of(epic, task1), manager.getHistory());
        manager.remove(1);
        assertEquals(List.of(epic), manager.getHistory());
        manager.remove(4);
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void test3_shouldSaveCorrectHistoryWhenDuplicating() {
        manager.addElement(task2);
        manager.addElement(subtask);
        manager.addElement(task1);
        manager.addElement(task3);
        Epic epic = new Epic("Name4", new ArrayList<>());
        epic.setId(4);
        manager.addElement(epic);
        assertEquals(List.of(task2, subtask, task1, task3, epic), manager.getHistory());
        manager.addElement(task3);
        manager.addElement(subtask);
        manager.addElement(subtask);
        manager.addElement(task2);
        assertEquals(List.of(task1, epic, task3, subtask, task2), manager.getHistory());
    }

    @Test
    public void test4_shouldRemoveElement() {
        manager.addElement(task2);
        manager.addElement(subtask);
        manager.addElement(task1);
        manager.addElement(task3);
        Epic epic = new Epic("Name4", new ArrayList<>());
        epic.setId(4);
        manager.addElement(epic);
        assertEquals(List.of(task2, subtask, task1, task3, epic), manager.getHistory());
        manager.remove(1);
        assertEquals(List.of(task2, subtask, task3, epic), manager.getHistory());
        manager.remove(2);
        assertEquals(List.of(subtask, task3, epic), manager.getHistory());
        manager.remove(4);
        assertEquals(List.of(subtask, task3), manager.getHistory());
    }
}
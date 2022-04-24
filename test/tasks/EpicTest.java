package tasks;

import managers.Managers;
import managers.TaskManager;
import managers.http_backup.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private TaskManager manager;

    @BeforeEach
    public void createManager() {
        manager = Managers.getDefault();
    }

    @AfterEach
    public void stopServer() {
        KVServer.stop();
    }

    @Test
    public void test1_shouldBeStatusEpicNewWithEmptySubtaskList() {
        Epic epic = new Epic("Name", new ArrayList<>());
        manager.addEpic(epic);
        assertTrue(manager.getEpic(1).getSubtaskList().isEmpty());
        assertEquals(Statuses.NEW, manager.getEpic(1).getStatus());
    }

    @Test
    public void test2_shouldBeStatusEpicNewWithSubtasksNew() {
        Epic epic = new Epic("Name", new ArrayList<>());
        manager.addEpic(epic);
        assertEquals(Statuses.NEW, manager.getEpic(1).getStatus());
        Subtask subtask1 = new Subtask("Подзадача №1", epic, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача №2", epic, "16.12.2021 14:18", 189);
        manager.addSubtask(subtask2);
        assertEquals(Statuses.NEW, manager.getEpic(1).getStatus());
    }

    @Test
    public void test3_shouldBeStatusEpicDoneWithSubtasksDone() {
        Epic epic = new Epic("Name", new ArrayList<>());
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача №1", epic, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача №2", epic, "16.12.2021 14:18", 189);
        manager.addSubtask(subtask2);
        assertEquals(Statuses.NEW, manager.getEpic(1).getStatus());
        subtask1.setStatus(Statuses.DONE);
        subtask2.setStatus(Statuses.DONE);
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);
        assertEquals(Statuses.DONE, manager.getEpic(1).getStatus());
    }

    @Test
    public void test4_shouldBeStatusEpicInProgressWithSubtaskDone() {
        Epic epic = new Epic("Name", new ArrayList<>());
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача №1", epic, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача №2", epic, "16.12.2021 14:18", 189);
        manager.addSubtask(subtask2);
        assertEquals(Statuses.NEW, manager.getEpic(1).getStatus());
        subtask2.setStatus(Statuses.DONE);
        manager.updateSubtask(subtask2);
        assertEquals(Statuses.IN_PROGRESS, manager.getEpic(1).getStatus());
    }

    @Test
    public void test5_shouldBeStatusEpicInProgressWithSubtasksInProgress() {
        Epic epic = new Epic("Name", new ArrayList<>());
        manager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача №1", epic, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Подзадача №2", epic, "16.12.2021 14:18", 189);
        manager.addSubtask(subtask2);
        assertEquals(Statuses.NEW, manager.getEpic(1).getStatus());
        subtask1.setStatus(Statuses.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        assertEquals(Statuses.IN_PROGRESS, manager.getEpic(1).getStatus());
        subtask2.setStatus(Statuses.IN_PROGRESS);
        manager.updateSubtask(subtask2);
        assertEquals(Statuses.IN_PROGRESS, manager.getEpic(1).getStatus());
    }

    @Test
    public void test6_shouldBeSetStartTimeFromSubtask() {
        Epic epic = new Epic("Name", new ArrayList<>());
        manager.addEpic(epic);
        assertNull(manager.getEpic(1).getStartTime());
        Subtask subtask1 = new Subtask("Подзадача №1", epic, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals("2021-08-09T12:00", String.valueOf(manager.getEpic(1).getStartTime()));
    }

    @Test
    public void test7_shouldBeSetDurationFromSubtask() {
        Epic epic = new Epic("Name", new ArrayList<>());
        manager.addEpic(epic);
        assertNull(manager.getEpic(1).getDuration());
        Subtask subtask1 = new Subtask("Подзадача №1", epic, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals("PT1H36M", String.valueOf(manager.getEpic(1).getDuration()));
    }

    @Test
    public void test8_shouldBeSetEndTimeFromSubtask() {
        Epic epic = new Epic("Name", new ArrayList<>());
        manager.addEpic(epic);
        assertNull(manager.getEpic(1).getEndTime());
        Subtask subtask1 = new Subtask("Подзадача №1", epic, "09.08.2021 12:00", 96);
        manager.addSubtask(subtask1);
        assertEquals("2021-08-09T13:36", String.valueOf(manager.getEpic(1).getEndTime()));
    }
}
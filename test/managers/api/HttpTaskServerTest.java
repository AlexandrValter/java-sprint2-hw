package managers.api;

import managers.Managers;
import managers.http_backup.HttpTaskManager;
import managers.http_backup.KVServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer server;
    private HttpTaskManager manager;
    private HttpClient client;
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
    public void createManagerAndServer() {
        this.manager = (HttpTaskManager) Managers.getDefault();
        this.server = new HttpTaskServer(this.manager);
        this.client = HttpClient.newHttpClient();
        server.startServer();
    }

    @AfterEach
    public void stopServers() {
        KVServer.stop();
        server.stopServer();
    }

    @Test
    public void test1_getTasksFromEmptyManager() {
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllTasks().isEmpty());
            assertEquals("[]", response.body());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2_getEpicsFromEmptyManager() {
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllEpics().isEmpty());
            assertEquals("[]", response.body());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3_getSubtasksFromEmptyManager() {
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllSubtasks().isEmpty());
            assertEquals("[]", response.body());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4_getTasksFromNotEmptyManager() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = "[{\"id\":1,\"name\":\"Задача №1\",\"description\":\"Задача №1 для тестов\"," +
                    "\"status\":\"NEW\",\"type\":\"TASK\",\"start time\":\"11.04.2022 09:00\",\"end time\":" +
                    "\"11.04.2022 11:00\",\"duration\":\"2h.0m.\"},{\"id\":2,\"name\":\"Задача №2\",\"description\":" +
                    "\"Задача №2 для тестов\",\"status\":\"NEW\",\"type\":\"TASK\",\"start time\":\"27.07.2021 11:35\"," +
                    "\"end time\":\"27.07.2021 14:14\",\"duration\":\"2h.39m.\"},{\"id\":3,\"name\":\"Задача №3\"," +
                    "\"description\":\"Задача №3 для тестов\",\"status\":\"NEW\",\"type\":\"TASK\",\"start time\":" +
                    "\"20.09.2021 19:20\",\"end time\":\"21.09.2021 02:25\",\"duration\":\"7h.5m.\"}]";
            assertEquals(json, response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5_getEpicsFromNotEmptyManager() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(new Subtask("Подзадача №1", epic2, "11.04.2022 09:00", 100));
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = "[{\"id\":1,\"name\":\"Эпик №1\",\"status\":\"NEW\",\"type\":\"EPIC\"},{\"id\":2," +
                    "\"name\":\"Эпик №2\",\"status\":\"NEW\",\"type\":\"EPIC\",\"start time\":\"11.04.2022 09:00\"," +
                    "\"end time\":\"11.04.2022 10:40\",\"duration\":\"1h.40m.\",\"subtask list\":[\"{\\\"id\\\":3," +
                    "\\\"name\\\":\\\"Подзадача №1\\\",\\\"status\\\":\\\"NEW\\\",\\\"type\\\":\\\"SUBTASK\\\"," +
                    "\\\"start time\\\":\\\"11.04.2022 09:00\\\",\\\"end time\\\":\\\"11.04.2022 10:40\\\"," +
                    "\\\"duration\\\":\\\"1h.40m.\\\",\\\"epic id\\\":2}\"]}]";
            assertEquals(json, response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test6_getSubtasksFromNotEmptyManager() {
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Подзадача №1", epic1, "11.04.2022 09:00", 100));
        manager.addSubtask(new Subtask("Подзадача №2", epic1, "11.04.2021 19:00", 153));
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = "[{\"id\":2,\"name\":\"Подзадача №1\",\"status\":\"NEW\",\"type\":\"SUBTASK\"," +
                    "\"start time\":\"11.04.2022 09:00\",\"end time\":\"11.04.2022 10:40\",\"duration\":\"1h.40m.\"," +
                    "\"epic id\":1},{\"id\":3,\"name\":\"Подзадача №2\",\"status\":\"NEW\",\"type\":\"SUBTASK\"," +
                    "\"start time\":\"11.04.2021 19:00\",\"end time\":\"11.04.2021 21:33\"," +
                    "\"duration\":\"2h.33m.\",\"epic id\":1}]";
            assertEquals(json, response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test7_getTaskFromEmptyManager() {
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllTasks().isEmpty());
            assertEquals("null", response.body());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test8_getEpicFromEmptyManager() {
        URI url = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllEpics().isEmpty());
            assertEquals("null", response.body());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test9_getSubtaskFromEmptyManager() {
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllSubtasks().isEmpty());
            assertEquals("null", response.body());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test10_getTaskFromNotEmptyManager() {
        manager.addTask(task1);
        manager.addTask(task2);
        URI url = URI.create("http://localhost:8080/tasks/task?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = "{\"id\":2,\"name\":\"Задача №2\",\"description\":\"Задача №2 для тестов\",\"status\":" +
                    "\"NEW\",\"type\":\"TASK\",\"start time\":\"27.07.2021 11:35\",\"end time\":\"27.07.2021 14:14\"," +
                    "\"duration\":\"2h.39m.\"}";
            assertFalse(manager.getAllTasks().isEmpty());
            assertEquals(json, response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test11_getEpicFromNotEmptyManager() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(new Subtask("Subtask", epic2, "01.01.2001 15:15", 123));
        URI url = URI.create("http://localhost:8080/tasks/epic?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = "{\"id\":2,\"name\":\"Эпик №2\",\"status\":\"NEW\",\"type\":\"EPIC\",\"start time\":" +
                    "\"01.01.2001 15:15\",\"end time\":\"01.01.2001 17:18\",\"duration\":\"2h.3m.\",\"subtask list\":" +
                    "[\"{\\\"id\\\":3,\\\"name\\\":\\\"Subtask\\\",\\\"status\\\":\\\"NEW\\\",\\\"type\\\":" +
                    "\\\"SUBTASK\\\",\\\"start time\\\":\\\"01.01.2001 15:15\\\",\\\"end time\\\":" +
                    "\\\"01.01.2001 17:18\\\",\\\"duration\\\":\\\"2h.3m.\\\",\\\"epic id\\\":2}\"]}";
            assertFalse(manager.getAllEpics().isEmpty());
            assertEquals(json, response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test12_getSubtaskFromNotEmptyManager() {
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Subtask", epic1, "01.01.2001 15:15", 123));
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = "{\"id\":2,\"name\":\"Subtask\",\"status\":\"NEW\",\"type\":\"SUBTASK\",\"start time\":" +
                    "\"01.01.2001 15:15\",\"end time\":\"01.01.2001 17:18\",\"duration\":\"2h.3m.\",\"epic id\":1}";
            assertFalse(manager.getAllSubtasks().isEmpty());
            assertEquals(json, response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test13_getSubtaskListFromEpicWhenManagerIsEmpty() {
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllEpics().isEmpty());
            assertEquals("null", response.body());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test14_getSubtaskListFromEpicWhenManagerIsNotEmpty() {
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Subtask1", epic1, "01.01.2001 15:15", 123));
        manager.addSubtask(new Subtask("Subtask2", epic1, "01.01.2002 15:15", 654));
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertFalse(manager.getAllEpics().isEmpty());
            String json = "[{\"id\":2,\"name\":\"Subtask1\",\"status\":\"NEW\",\"type\":\"SUBTASK\"," +
                    "\"start time\":\"01.01.2001 15:15\",\"end time\":\"01.01.2001 17:18\",\"duration\":\"2h.3m.\"," +
                    "\"epic id\":1},{\"id\":3,\"name\":\"Subtask2\",\"status\":\"NEW\",\"type\":\"SUBTASK\"," +
                    "\"start time\":\"01.01.2002 15:15\",\"end time\":\"02.01.2002 02:09\"," +
                    "\"duration\":\"10h.54m.\",\"epic id\":1}]";
            assertEquals(json, response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test15_getHistoryListFromEmptyManager() {
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getHistoryList().isEmpty());
            assertEquals("[]", response.body());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test16_getHistoryListFromNotEmptyManager() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        assertTrue(manager.getHistoryList().isEmpty());
        manager.getTask(1);
        manager.getTask(3);
        manager.getTask(2);
        assertFalse(manager.getHistoryList().isEmpty());
        URI url = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = "[{\"id\":1,\"name\":\"Задача №1\",\"description\":\"Задача №1 для тестов\"," +
                    "\"status\":\"NEW\",\"type\":\"TASK\",\"start time\":\"11.04.2022 09:00\",\"end time\":" +
                    "\"11.04.2022 11:00\",\"duration\":\"2h.0m.\"},{\"id\":3,\"name\":\"Задача №3\",\"description\":" +
                    "\"Задача №3 для тестов\",\"status\":\"NEW\",\"type\":\"TASK\",\"start time\":\"20.09.2021 19:20\"," +
                    "\"end time\":\"21.09.2021 02:25\",\"duration\":\"7h.5m.\"},{\"id\":2,\"name\":\"Задача №2\"," +
                    "\"description\":\"Задача №2 для тестов\",\"status\":\"NEW\",\"type\":\"TASK\",\"start time\":" +
                    "\"27.07.2021 11:35\",\"end time\":\"27.07.2021 14:14\",\"duration\":\"2h.39m.\"}]";
            assertEquals(json, response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test17_getPrioritizedTaskFromEmptyManager() {
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getPrioritizedTasks().isEmpty());
            assertEquals("[]", response.body());
            assertEquals(404, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test18_getPrioritizedTaskFromNotEmptyManager() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        try {
            assertFalse(manager.getPrioritizedTasks().isEmpty());
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = "[{\"id\":2,\"name\":\"Задача №2\",\"description\":\"Задача №2 для тестов\",\"status\":" +
                    "\"NEW\",\"type\":\"TASK\",\"start time\":\"27.07.2021 11:35\",\"end time\":\"27.07.2021 14:14\"," +
                    "\"duration\":\"2h.39m.\"},{\"id\":3,\"name\":\"Задача №3\",\"description\":" +
                    "\"Задача №3 для тестов\",\"status\":\"NEW\",\"type\":\"TASK\",\"start time\":\"20.09.2021 19:20\"," +
                    "\"end time\":\"21.09.2021 02:25\",\"duration\":\"7h.5m.\"},{\"id\":1,\"name\":\"Задача №1\"," +
                    "\"description\":\"Задача №1 для тестов\",\"status\":\"NEW\",\"type\":\"TASK\",\"start time\":" +
                    "\"11.04.2022 09:00\",\"end time\":\"11.04.2022 11:00\",\"duration\":\"2h.0m.\"}]";
            assertEquals(json, response.body());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test19_addEmptyTask() {
        assertEquals(0, manager.getAllTasks().size());
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = "";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllTasks().isEmpty());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test20_addTask() {
        assertEquals(0, manager.getAllTasks().size());
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = "{\"name\":\"Задача №2\",\"description\":\"Задача №2 для тестов\"," +
                "\"start time\":\"27.07.2021 11:35\",\"duration\":100}";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(1, manager.getAllTasks().size());
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test21_addEmptyEpic() {
        assertEquals(0, manager.getAllEpics().size());
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = "";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllEpics().isEmpty());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test22_addEpic() {
        assertEquals(0, manager.getAllEpics().size());
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = "{\"name\":\"Задача №1\"}";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(1, manager.getAllEpics().size());
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test23_addEmptySubtask() {
        assertEquals(0, manager.getAllSubtasks().size());
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = "";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllSubtasks().isEmpty());
            assertEquals(400, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test24_addSubtask() {
        assertEquals(0, manager.getAllSubtasks().size());
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        manager.addEpic(epic1);
        String json = "{\"name\":\"Сабтаск №1\",\"epic id\":1,\"start time\":\"11.04.2022 09:00\",\"duration\":100}";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(1, manager.getAllSubtasks().size());
            assertEquals(201, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test25_updateTask() {
        manager.addTask(task1);
        assertEquals("Task{name='Задача №1', id=1, description='Задача №1 для тестов', " +
                "status='NEW', start time='11.04.2022 09:00', duration='2h.0m.', " +
                "end time='11.04.2022 11:00'}", manager.getTask(1).toString());
        URI url = URI.create("http://localhost:8080/tasks/task");
        String json = "{\"id\":1,\"name\":\"Задача №1(измененная)\",\"description\":\"Задача №1 для тестов (измененная)\"," +
                "\"status\":\"IN_PROGRESS\",\"type\":\"TASK\",\"start time\":\"11.04.2022 09:00\",\"end time\":" +
                "\"11.04.2022 11:00\",\"duration\":\"2h.0m.\"}";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(1, manager.getAllTasks().size());
            assertEquals("Task{name='Задача №1(измененная)', id=1, description='Задача №1 для тестов " +
                    "(измененная)', status='IN_PROGRESS', start time='11.04.2022 09:00', duration='2h.0m.', " +
                    "end time='11.04.2022 11:00'}", manager.getTask(1).toString());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test26_updateEpic() {
        manager.addEpic(epic1);
        assertEquals("Epic{name='Эпик №1', id=1, status=NEW}, subtaskList=null",
                manager.getEpic(1).toString());
        URI url = URI.create("http://localhost:8080/tasks/epic");
        String json = "{\"id\":1,\"name\":\"Эпик №1(измененный)\",\"status\":\"NEW\",\"type\":\"EPIC\"}";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(1, manager.getAllEpics().size());
            assertEquals("Epic{name='Эпик №1(измененный)', id=1, status=NEW}, subtaskList=null",
                    manager.getEpic(1).toString());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test27_updateSubtask() {
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Сабтаск №1", epic1, "11.04.2022 09:00", 111));
        assertEquals("Subtask{name='Сабтаск №1', id=2, status=NEW, epic=Эпик №1, " +
                        "start time='11.04.2022 09:00', duration='1h.51m.', end time='11.04.2022 10:51'} ",
                manager.getSubtask(2).toString());
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        String json = "{\"id\":2, \"name\":\"Сабтаск №1(измененный)\",\"status\":\"IN_PROGRESS\",\"type\":\"SUBTASK\"," +
                "\"end time\":\"11.04.2022 10:51\",\"epic id\":1,\"start time\":\"11.04.2022 09:00\",\"duration\":111}";
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(1, manager.getAllSubtasks().size());
            assertEquals("Subtask{name='Сабтаск №1(измененный)', id=2, status=IN_PROGRESS, epic=Эпик №1, " +
                            "start time='11.04.2022 09:00', duration='1h.51m.', end time='11.04.2022 10:51'} ",
                    manager.getSubtask(2).toString());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test28_deleteTaskWhenManagerIsEmpty() {
        assertEquals(0, manager.getAllTasks().size());
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllTasks().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test29_deleteTaskWhenManagerIsNotEmpty() {
        manager.addTask(task1);
        assertEquals(1, manager.getAllTasks().size());
        URI url = URI.create("http://localhost:8080/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllTasks().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test30_deleteEpicWhenManagerIsEmpty() {
        assertEquals(0, manager.getAllEpics().size());
        URI url = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllEpics().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test31_deleteEpicWhenManagerIsNotEmpty() {
        manager.addEpic(epic1);
        assertEquals(1, manager.getAllEpics().size());
        URI url = URI.create("http://localhost:8080/tasks/epic?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllEpics().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test32_deleteSubtaskWhenManagerIsEmpty() {
        assertEquals(0, manager.getAllSubtasks().size());
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllSubtasks().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test33_deleteSubtaskWhenManagerIsNotEmpty() {
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Subtask1", epic1, "01.01.2001 15:15", 123));
        assertEquals(1, manager.getAllSubtasks().size());
        URI url = URI.create("http://localhost:8080/tasks/subtask?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllSubtasks().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test34_clearTasksWhenManagerIsEmpty() {
        assertEquals(0, manager.getAllTasks().size());
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllTasks().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test35_clearTasksWhenManagerIsNotEmpty() {
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        assertEquals(3, manager.getAllTasks().size());
        URI url = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllTasks().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test36_clearEpicsWhenManagerIsEmpty() {
        assertEquals(0, manager.getAllEpics().size());
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllEpics().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test37_clearEpicsWhenManagerIsNotEmpty() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        assertEquals(3, manager.getAllEpics().size());
        URI url = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllEpics().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test36_clearSubtasksWhenManagerIsEmpty() {
        assertEquals(0, manager.getAllSubtasks().size());
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllSubtasks().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test37_clearSubtasksWhenManagerIsNotEmpty() {
        manager.addEpic(epic1);
        manager.addSubtask(new Subtask("Subtask1", epic1, "01.01.2001 15:15", 123));
        manager.addSubtask(new Subtask("Subtask2", epic1, "01.01.2002 15:15", 123));
        manager.addSubtask(new Subtask("Subtask3", epic1, "01.01.2003 15:15", 123));
        assertEquals(3, manager.getAllSubtasks().size());
        URI url = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertTrue(manager.getAllSubtasks().isEmpty());
            assertEquals(200, response.statusCode());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
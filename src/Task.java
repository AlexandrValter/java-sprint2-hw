public class Task {
    protected int id;
    protected String name;
    private String description;
    protected String status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = "NEW";
    }

    public Task() {
    }

    public static int getId(Task task) {
        return task.id;
    }

    public static void setId(int id, Task task) {
        task.id = id;
    }

    public static void setStatus(String status, Task task) {
        task.status = status;
    }
}

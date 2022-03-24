package tasks;

public class Task {
    private int id;
    private String name;
    private String description;
    private Statuses status;
    private TypeOfTasks type;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Statuses.NEW;
        this.type = TypeOfTasks.TASK;
    }

    public Task(int id, String name, String description, Statuses status, TypeOfTasks type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public void setType(TypeOfTasks type) {
        this.type = type;
    }

    public TypeOfTasks getType() {
        return type;
    }

    public Statuses getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public String toString(Task task) {
        return task.getId() + ","
                + task.getType() + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getDescription();
    }

    public Task fromString(String[] value) {
        return new Task(Integer.valueOf(value[0]),
                value[2],
                value[4],
                Statuses.valueOf(value[3]),
                TypeOfTasks.valueOf(value[1]));
    }
}
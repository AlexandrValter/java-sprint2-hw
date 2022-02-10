package tasks;

public class Task {
    private int id;
    private String name;
    private String description;
    private Statuses status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Statuses.NEW;
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

    public Statuses getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
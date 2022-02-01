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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

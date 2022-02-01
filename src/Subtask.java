public class Subtask extends Task {
    protected Epic epic;

    public Subtask(String name, Epic epic) {
        this.name = name;
        this.epic = epic;
        status = "NEW";
    }
}

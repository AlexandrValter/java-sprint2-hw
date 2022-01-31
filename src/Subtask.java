public class Subtask extends Task {
    Integer epicId;

    public Subtask(String name, Integer epicId) {
        this.name = name;
        this.epicId = epicId;
        status = "NEW";
    }
}

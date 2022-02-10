package tasks;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, Epic epic) {
        this.setName(name);
        this.epic = epic;
        this.setStatus(Statuses.NEW);
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + super.getName() + '\'' +
                ", id=" + super.getId() +
                ", status=" + super.getStatus() +
                ", epic=" + getEpic().getName() +
                "} ";
    }
}

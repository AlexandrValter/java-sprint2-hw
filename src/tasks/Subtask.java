package tasks;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, Epic epic) {
        this.setName(name);
        this.epic = epic;
        this.setStatus(Statuses.NEW);
        this.setType(TypeOfTasks.SUBTASK);
    }

    public Subtask(int id, String name, String description, Statuses status, TypeOfTasks type) {
        super(id, name, description, status, type);
    }

    public Subtask() {
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
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

    public String toString(Subtask subtask) {
        return super.toString(subtask) + "," + subtask.getEpic().getId();
    }

    @Override
    public Subtask fromString(String[] value) {
        return new Subtask(Integer.valueOf(value[0]),
                value[2],
                value[4],
                Statuses.valueOf(value[3]),
                TypeOfTasks.valueOf(value[1]));
    }
}

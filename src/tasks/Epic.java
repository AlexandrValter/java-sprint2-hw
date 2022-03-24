package tasks;

import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtaskList;

    public Epic(String name, List<Subtask> subtaskList) {
        this.setName(name);
        this.subtaskList = subtaskList;
        this.setStatus(Statuses.NEW);
        this.setType(TypeOfTasks.EPIC);
    }

    public Epic(int id, String name, String description, Statuses status, TypeOfTasks type) {
        super(id, name, description, status, type);
    }

    public Epic() {
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
    }

    public void addSubtaskToEpic(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public void deleteSubtaskFromEpic(Subtask subtask) {
        subtaskList.remove(subtask);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + super.getName() + '\'' +
                ", id=" + super.getId() +
                ", status=" + super.getStatus() +
                "}";
        if (subtaskList != null) {
            result = result + ", subtaskList.size=" + getSubtaskList().size();
        } else {
            result = result + ", subtaskList=null";
        }
        return result;
    }

    public String toString(Epic epic) {
        return super.toString(epic);
    }

    @Override
    public Epic fromString(String[] value) {
        return new Epic(Integer.valueOf(value[0]),
                value[2],
                value[4],
                Statuses.valueOf(value[3]),
                TypeOfTasks.valueOf(value[1]));
    }
}
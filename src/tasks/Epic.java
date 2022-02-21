package tasks;

import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtaskList;

    public Epic(String name, List<Subtask> subtaskList) {
        this.setName(name);
        this.subtaskList = subtaskList;
        this.setStatus(Statuses.NEW);
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
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
}
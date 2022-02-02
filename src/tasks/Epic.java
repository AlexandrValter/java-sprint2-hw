package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtaskList;

    public Epic(String name, ArrayList<Subtask> subtaskList) {
        this.setName(name);
        this.subtaskList = subtaskList;
        this.setStatus("NEW");
    }

    public ArrayList<Subtask> getSubtaskList() {
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
                "} ";
        if (subtaskList != null) {
            result = result + ", subtaskList.size=" + getSubtaskList().size();
        } else {
            result = result + ", subtaskList=null";
        }
        return result;
    }
}


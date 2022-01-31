import java.util.ArrayList;

class Epic extends Task {
    protected ArrayList<Subtask> subtaskList;

    public Epic(String name, ArrayList<Subtask> subtaskList) {
        this.name = name;
        this.subtaskList = subtaskList;
        status = "NEW";
    }

    public ArrayList<Subtask> getSubtaskList() {
        return subtaskList;
    }
}


package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtaskList;
    private LocalDateTime endTime;

    public Epic(String name, List<Subtask> subtaskList) {
        this.setName(name);
        this.subtaskList = subtaskList;
        this.setStatus(Statuses.NEW);
        this.setType(TypeOfTasks.EPIC);
        setStartTime();
        setDuration();
    }

    public Epic(int id,
                String name,
                String description,
                Statuses status,
                TypeOfTasks type,
                LocalDateTime startTime,
                Duration duration
    ) {
        super(id, name, description, status, type, startTime, duration);
        this.subtaskList = new ArrayList<>();
        this.endTime = null;
    }

    public Epic() {
    }

    public void setStartTime() {
        LocalDateTime startTime = null;
        if (!subtaskList.isEmpty()) {
            if (subtaskList.size() == 1) {
                this.setStartTime(subtaskList.get(0).getStartTime());
            } else {
                startTime = subtaskList.get(0).getStartTime();
                for (int i = 1; i < subtaskList.size(); i++) {
                    if (startTime.isAfter(subtaskList.get(i).getStartTime())) {
                        startTime = subtaskList.get(i).getStartTime();
                    }
                }
                this.setStartTime(startTime);
            }
        }
    }

    public void setDuration() {
        Duration epicDuration = Duration.ZERO;
        if (!subtaskList.isEmpty()) {
            for (Subtask task : subtaskList) {
                epicDuration = epicDuration.plusMinutes((task.getDuration()).toMinutes());
            }
            this.setDuration(epicDuration);
        }
    }

    public void setEndTime() {
        LocalDateTime time;
        if (!subtaskList.isEmpty()) {
            if (subtaskList.size() == 1) {
                endTime = subtaskList.get(0).getEndTime();
            } else {
                time = subtaskList.get(0).getEndTime();
                for (int i = 1; i < subtaskList.size(); i++) {
                    if (time.isBefore(subtaskList.get(i).getEndTime())) {
                        time = subtaskList.get(i).getEndTime();
                    }
                }
                endTime = time;
            }
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void setSubtaskList(List<Subtask> subtaskList) {
        this.subtaskList = subtaskList;
        setStartTime();
        setDuration();
        setEndTime();
    }

    public void addSubtaskToEpic(Subtask subtask) {
        subtaskList.add(subtask);
        setStartTime();
        setDuration();
        setEndTime();
    }

    public void deleteSubtaskFromEpic(Subtask subtask) {
        subtaskList.remove(subtask);
        setStartTime();
        setDuration();
        setEndTime();
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + super.getName() + '\'' +
                ", id=" + super.getId() +
                ", status=" + super.getStatus() +
                "}";
        if (subtaskList.size() > 0) {
            result = result + ", subtaskList.size=" + getSubtaskList().size() +
                    ", start time='" + super.getStartTime().format(super.getFormatterTime()) +
                    ", duration='" + super.getDuration().toHours() + "h." + super.getDuration().toMinutesPart() + "m.'" +
                    ", end time='" + endTime.format(super.getFormatterTime()) + '\'';
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
        if (value[5].equals("null") || value[6].equals("null")) {
            Epic epic = new Epic(value[2], new ArrayList<>());
            epic.setStatus(Statuses.valueOf(value[3]));
            epic.setDescription(value[4]);
            epic.setType(TypeOfTasks.valueOf(value[1]));
            epic.setId(Integer.valueOf(value[0]));
            return epic;
        } else {
            return new Epic(Integer.valueOf(value[0]),
                    value[2],
                    value[4],
                    Statuses.valueOf(value[3]),
                    TypeOfTasks.valueOf(value[1]),
                    LocalDateTime.parse(value[5]),
                    Duration.parse(value[6]));
        }
    }
}
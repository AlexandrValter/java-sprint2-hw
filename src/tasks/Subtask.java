package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, Epic epic, String startTime, int duration) {
        this.setName(name);
        this.epic = epic;
        this.setStatus(Statuses.NEW);
        this.setType(TypeOfTasks.SUBTASK);
        this.setStartTime(LocalDateTime.parse(startTime, super.getFormatterTime()));
        this.setDuration(Duration.ofMinutes(duration));
        this.setEndTime(this.getEndTime());
    }

    public Subtask(int id,
                   String name,
                   String description,
                   Statuses status,
                   TypeOfTasks type,
                   LocalDateTime startTime,
                   Duration duration
    ) {
        super(id, name, description, status, type, startTime, duration);
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
                ", start time='" + super.getStartTime().format(super.getFormatterTime()) + '\'' +
                ", duration='" + super.getDuration().toHours() + "h." + super.getDuration().toMinutesPart() + "m." + '\'' +
                ", end time='" + super.getEndTime().format(super.getFormatterTime()) + '\'' +
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
                TypeOfTasks.valueOf(value[1]),
                LocalDateTime.parse(value[5]),
                Duration.parse(value[6]));
    }
}
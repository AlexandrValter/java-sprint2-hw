package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private int id;
    private String name;
    private String description;
    private Statuses status;
    private TypeOfTasks type;
    private LocalDateTime startTime;
    private final DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private Duration duration;
    private LocalDateTime endTime;

    public Task(String name, String description, String startTime, int duration) {
        this.name = name;
        this.description = description;
        status = Statuses.NEW;
        this.type = TypeOfTasks.TASK;
        this.startTime = LocalDateTime.parse(startTime, formatterTime);
        this.duration = Duration.ofMinutes(duration);
        this.endTime = getEndTime();
    }

    public LocalDateTime getEndTime() {
        return this.startTime.plusHours(duration.toHours()).plusMinutes(duration.toMinutesPart());
    }

    public Task(
            int id,
            String name,
            String description,
            Statuses status,
            TypeOfTasks type,
            LocalDateTime startTime,
            Duration duration
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Statuses status) {
        this.status = status;
    }

    public void setType(TypeOfTasks type) {
        this.type = type;
    }

    public TypeOfTasks getType() {
        return type;
    }

    public Statuses getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public DateTimeFormatter getFormatterTime() {
        return formatterTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", start time='" + startTime.format(formatterTime) + '\'' +
                ", duration='" + duration.toHours() + "h." + duration.toMinutesPart() + "m." + '\'' +
                ", end time='" + endTime.format(formatterTime) + '\'' +
                '}';
    }

    public String toString(Task task) {
        return task.getId() + ","
                + task.getType() + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getDescription() + ","
                + task.getStartTime() + ","
                + task.getDuration();
    }

    public Task fromString(String[] value) {
        return new Task(Integer.valueOf(value[0]),
                value[2],
                value[4],
                Statuses.valueOf(value[3]),
                TypeOfTasks.valueOf(value[1]),
                LocalDateTime.parse(value[5]),
                Duration.parse(value[6]));
    }
}
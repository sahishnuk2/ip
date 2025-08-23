import java.time.LocalDateTime;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), this.from, this.to);
    }

    @Override
    public String toSaveString() {
        int status = super.getIsDone() ? 1 : 0;
        return String.format("E @@@ %d @@@ %s @@@ %s @@@ %s", status, super.getTaskDescription(), this.from, this.to);
    }
}

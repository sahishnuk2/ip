package sharva.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task that must be completed by a specific date and time.
 * A deadline has a due date (by) when the task should be finished.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Creates a new Deadline task with the given description and due date.
     *
     * @param description The description of the deadline task
     * @param by The date and time when the task should be completed
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("[D]%s (by: %s)", super.toString(), this.by.format(formatter));
    }

    @Override
    public String toSaveString() {
        int status = super.getIsDone() ? 1 : 0;
        DateTimeFormatter saveFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        return String.format("D @@@ %d @@@ %s @@@ %s",
                status,
                super.getTaskDescription(),
                this.by.format(saveFormatter));
    }
}

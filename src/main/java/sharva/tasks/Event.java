package sharva.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task that occurs between two specific date-time periods.
 * An event has a start time (from) and an end time (to).
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates a new Event task with the given description and time period.
     *
     * @param description The description of the event
     * @param from The start date and time of the event
     * @param to The end date and time of the event
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("[E]%s (from: %s to: %s)",
                super.toString(),
                this.from.format(formatter),
                this.to.format(formatter));
    }

    @Override
    public String toSaveString() {
        int status = super.getIsDone() ? 1 : 0;
        DateTimeFormatter saveFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm");
        return String.format("E @@@ %d @@@ %s @@@ %s @@@ %s",
                status,
                super.getTaskDescription(),
                this.from.format(saveFormatter),
                this.to.format(saveFormatter));
    }
}

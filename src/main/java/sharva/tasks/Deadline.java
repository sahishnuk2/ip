package sharva.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.by.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }

    @Override
    public String toSaveString() {
        int status = super.getIsDone() ? 1 : 0;
        return String.format("D @@@ %d @@@ %s @@@ %s", status, super.getTaskDescription(), this.by.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HHmm")));
    }
}

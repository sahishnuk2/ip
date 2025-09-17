package sharva.tasks;

/**
 * Represents a simple todo task without any specific deadline or time constraints.
 * This is the basic task type in the task management system.
 */
public class ToDo extends Task {

    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toSaveString() {
        int status = super.getIsDone() ? 1 : 0;
        return String.format("T @@@ %d @@@ %s", status, super.getTaskDescription());
    }
}

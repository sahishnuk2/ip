package sharva.tasks;

import sharva.exceptions.InvalidArgumentsException;
import sharva.exceptions.SharvaException;

/**
 * Represents an abstract task with a description and completion status.
 * This class serves as the base class for all types of tasks in the system.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Constructs a new Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done
     * @throws SharvaException if task is already marked
     */
    public void markAsDone() throws SharvaException {
        if (isDone) {
            throw new InvalidArgumentsException("Completing a completed task? How hardworking");
        }
        this.isDone = true;
    }

    /**
     * Unmarks the task as not done
     * @throws SharvaException if task in already unmarked
     */
    public void undoTask() throws SharvaException {
        if (!isDone) {
            throw new InvalidArgumentsException("Bro, the task is not even completed");
        }
        this.isDone = false;
    }

    public boolean contains(String input) {
        return this.description.contains(input);
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public String getTaskDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        String status = isDone ? "[X] " : "[ ] ";
        return status + this.description;
    }

    /**
     * Converts this task into a string format suitable for saving to a file.
     * @return the string representation of the task for storage
     */
    public abstract String toSaveString();
}

package sharva;

public abstract class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() throws SharvaException {
        if (isDone) {
            throw new InvalidArgumentsException("Completing a completed task? How hardworking");
        }
        this.isDone = true;
    }

    public void undoTask() throws SharvaException {
        if (!isDone) {
            throw new InvalidArgumentsException("Bro, the task is not even completed");
        }
        this.isDone = false;
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

    public abstract String toSaveString();
}

public class Task {
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

    @Override
    public String toString() {
        String status = isDone ? "[X] " : "[ ] ";
        return status + this.description;
    }
}

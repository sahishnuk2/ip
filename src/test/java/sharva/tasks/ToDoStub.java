package sharva.tasks;

/**
 * Stub class for ToDo tasks used in testing.
 * Provides a controlled implementation for testing purposes.
 */
public class ToDoStub extends ToDo {
    private boolean isDoneStub;

    /**
     * Creates a new ToDoStub with a default description for testing.
     */
    public ToDoStub() {
        super("todo task");

    }

    @Override
    public void markAsDone() {
        isDoneStub = true;
    }

    @Override
    public void undoTask() {
        isDoneStub = false;
    }

    @Override
    public String toString() {
        String status = isDoneStub ? "[X] " : "[ ] ";
        return status + "todo task";
    }
}

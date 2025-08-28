package sharva.tasks;

public class ToDoStub extends ToDo {
    private boolean isDoneStub;

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

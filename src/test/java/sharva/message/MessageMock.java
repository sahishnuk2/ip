package sharva.message;

import sharva.tasks.Task;

public class MessageMock implements MessageService {

    public Task lastTask = null;      // tracks the last task used
    public String lastAction = null;  // "mark", "unmark", "add", "delete"
    public int numOfTasks = 0;        // for add/delete
    public String lastInput = null;   // for echo
    public boolean isEchoCalled = false;

    @Override
    public void mark(Task task) {
        lastTask = task;
        lastAction = "mark";
    }

    @Override
    public void unmark(Task task) {
        lastTask = task;
        lastAction = "unmark";
    }

    @Override
    public void addTask(Task task, int numberOfTasks) {
        lastTask = task;
        lastAction = "add";
        numOfTasks = numberOfTasks;
    }

    @Override
    public void deleteTask(Task task, int numberOfTasks) {
        lastTask = task;
        lastAction = "delete";
        numOfTasks = numberOfTasks;
    }

    @Override
    public void echo(String input) {
        lastInput = input;
        isEchoCalled = true;
    }

    @Override
    public void sayHello() {

    }

    @Override
    public void list(String input) {
        echo(input);
    }

    public void reset() {
        lastTask = null;
        lastAction = null;
        numOfTasks = 0;
        lastInput = null;
        isEchoCalled = false;
    }
}

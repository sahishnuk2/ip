package sharva.message;

import sharva.tasks.Task;

/**
 * Mock implementation of MessageService for testing purposes.
 * Tracks method calls and arguments to verify proper interaction with the message service.
 */
public class MessageMock implements MessageService {

    private Task lastTask = null; // tracks the last task used
    private String lastAction = null; // "mark", "unmark", "add", "delete"
    private int numOfTasks = 0; // for add/delete
    private String lastInput = null; // for echo
    private boolean isEchoCalled = false;

    @Override
    public void markUI(Task task) {
        lastTask = task;
        lastAction = "mark";
    }

    @Override
    public void unmarkUI(Task task) {
        lastTask = task;
        lastAction = "unmark";
    }

    @Override
    public void addTaskUI(Task task, int numberOfTasks) {
        lastTask = task;
        lastAction = "add";
        numOfTasks = numberOfTasks;
    }

    @Override
    public void deleteTaskUI(Task task, int numberOfTasks) {
        lastTask = task;
        lastAction = "delete";
        numOfTasks = numberOfTasks;
    }

    @Override
    public void echoUI(String input) {
        lastInput = input;
        isEchoCalled = true;
    }

    @Override
    public void sayHello() {

    }

    @Override
    public void listTasksUI(String input) {
        echoUI(input);
    }

    /**
     * Resets all tracked state to initial values for test cleanup.
     */
    public void reset() {
        lastTask = null;
        lastAction = null;
        numOfTasks = 0;
        lastInput = null;
        isEchoCalled = false;
    }

    public Task getLastTask() {
        return lastTask;
    }

    public String getLastAction() {
        return lastAction;
    }

    public int getNumOfTasks() {
        return numOfTasks;
    }

    public String getLastInput() {
        return lastInput;
    }

    public boolean getIsEchoCalled() {
        return isEchoCalled;
    }
}

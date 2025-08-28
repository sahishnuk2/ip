package sharva.tasklist;

import sharva.message.MessageService;
import sharva.tasks.Task;
import sharva.exceptions.InvalidIndexException;
import sharva.exceptions.SharvaException;
import sharva.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the list of tasks and their associated operations.
 */
public class TaskList implements TaskListService{
    private final List<Task> tasks;
    private final MessageService message;

    public TaskList(List<Task> tasks, MessageService message) {
        this.tasks = tasks;
        this.message = message;
    }

    public TaskList(MessageService message) {
        this.tasks = new ArrayList<>();
        this.message = message;
    }

    /**
     * Marks the task at the specified index as completed.
     * @param index the position of the task in tasks (0 based)
     * @throws SharvaException if the index is invalid
     */
    public void mark(int index) throws SharvaException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidIndexException();
        }
        tasks.get(index).markAsDone();
        message.mark(tasks.get(index));
    }

    /**
     * Marks the task at the specified index as not done.
     * @param index the position of the task in tasks (0 based)
     * @throws SharvaException if the index is invalid
     */
    public void unmark(int index) throws SharvaException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidIndexException();
        }
        tasks.get(index).undoTask();
        message.unmark(tasks.get(index));
    }

    /**
     * Add the specified task to tasks.
     * @param task the task to be added
     */
    public void addTask(Task task) {
        tasks.add(task);
        message.addTask(task, tasks.size());
    }

    /**
     * Deletes the task at the specified index.
     * @param index the position of the task in tasks (0 based)
     * @throws SharvaException if the index is invalid
     */
    public void delete(int index) throws SharvaException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidIndexException();
        }
        Task task = tasks.get(index);
        tasks.remove(index);
        message.deleteTask(task, tasks.size());
    }

    /**
     * Displays all tasks in tasks with their corresponding indices.
     */
    public void list() {
        StringBuilder result = new StringBuilder("    Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            result.append("\n").append(String.format("    %d. %s", i + 1, tasks.get(i).toString()));
        }
        message.echo(result.toString());
    }

    public List<Task> getTasks() {
        return tasks;
    }
}

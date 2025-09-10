package sharva.tasklist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import sharva.exceptions.InvalidIndexException;
import sharva.exceptions.SharvaException;
import sharva.message.MessageService;
import sharva.tasks.Task;


/**
 * Manages the list of tasks and their associated operations.
 */
public class TaskList implements TaskListService {
    private final List<Task> tasks;
    private final MessageService message;

    /**
     * Constructs a TaskList with an existing list of tasks.
     * @param tasks the list of tasks to initialize with
     * @param message the message service for displaying messages
     */
    public TaskList(List<Task> tasks, MessageService message) {
        this.tasks = tasks;
        this.message = message;
    }

    /**
     * Constructs a TaskList with an empty list of tasks.
     * @param message the message service for displaying messages
     */
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
        String result = IntStream.range(0, tasks.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, tasks.get(i).toString()))
                .reduce("Here are the tasks in your list:", (acc, task) -> acc + "\n" + task);
        message.list(result);
    }

    /**
     * Finds and displays all tasks that contain the specified input string.
     * @param input the string to search for in task descriptions
     */
    public void find(String input) {
        List<Task> filteredList = tasks.stream()
                .filter(task -> task.contains(input))
                .toList();

        String result = IntStream.range(0, tasks.size())
                .mapToObj(i -> String.format("%d. %s", i + 1, tasks.get(i).toString()))
                .reduce("Here are the matching tasks in your list:", (acc, task) -> acc + "\n" + task);
        message.list(result);
    }


    /**
     * Returns the list of tasks.
     * @return the list of tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }
}

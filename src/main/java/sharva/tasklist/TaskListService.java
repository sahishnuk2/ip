package sharva.tasklist;

import java.util.List;

import sharva.exceptions.SharvaException;
import sharva.tasks.Task;

/**
 * Service interface for managing a collection of tasks.
 * Provides operations for task manipulation including adding, deleting,
 * marking completion status, listing, and searching tasks.
 */
public interface TaskListService {

    public void mark(int index) throws SharvaException;
    public void unmark(int index) throws SharvaException;
    public void addTask(Task task);
    public void delete(int index) throws SharvaException;
    public void list();
    public List<Task> getTasks();
    public void find(String input);
}

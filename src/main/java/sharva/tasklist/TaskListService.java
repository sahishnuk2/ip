package sharva.tasklist;

import sharva.exceptions.SharvaException;
import sharva.tasks.Task;

import java.util.List;

public interface TaskListService {

    public void mark(int index) throws SharvaException;
    public void unmark(int index) throws SharvaException;
    public void addTask(Task task);
    public void delete(int index)  throws SharvaException;
    public void list();
    public List<Task> getTasks();
    public void find(String input);
}

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks = new ArrayList<>();

    public TaskList(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void mark(int index) throws SharvaException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidIndexException();
        }
        tasks.get(index).markAsDone();
    }

    public void unmark(int index) throws SharvaException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidIndexException();
        }
        tasks.get(index).undoTask();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void delete(int index) {
        tasks.remove(index);
    }

    public String list() {
        StringBuilder result = new StringBuilder("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            result.append("\n").append(String.format("%d. %s", i + 1, tasks.get(i).toString()));
        }
        return result.toString();
    }
}

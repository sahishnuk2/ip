import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks = new ArrayList<>();
    private Message message;

    public TaskList(List<Task> tasks, Message message) {
        this.tasks = tasks;
        this.message = message;
    }

    public void mark(int index) throws SharvaException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidIndexException();
        }
        tasks.get(index).markAsDone();
        message.mark(tasks.get(index));
    }

    public void unmark(int index) throws SharvaException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidIndexException();
        }
        tasks.get(index).undoTask();
        message.unmark(tasks.get(index));
    }

    public void addTask(Task task) {
        tasks.add(task);
        message.addTask(task, tasks.size());
    }

    public void delete(int index) throws SharvaException {
        if (index < 0 || index >= tasks.size()) {
            throw new InvalidIndexException();
        }
        tasks.remove(index);
    }

    public String list() {
        StringBuilder result = new StringBuilder("    Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            result.append("\n").append(String.format("    %d. %s", i + 1, tasks.get(i).toString()));
        }
        return result.toString();
    }

    public List<Task> getTasks() {
        return tasks;
    }
}

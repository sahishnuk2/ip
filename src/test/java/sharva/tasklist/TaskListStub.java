package sharva.tasklist;

import java.util.ArrayList;
import java.util.List;

import sharva.tasks.Task;

/**
 * Stub implementation of TaskListService for testing purposes.
 * Tracks method calls and provides controlled behavior for unit tests.
 */
public class TaskListStub implements TaskListService {
    private final List<String> calledMethods = new ArrayList<>();

    public List<String> getCalledMethods() {
        return calledMethods;
    }

    @Override
    public void mark(int index) {
        calledMethods.add("mark:" + (index + 1));
    }

    @Override
    public void unmark(int index) {
        calledMethods.add("unmark:" + (index + 1));
    }

    @Override
    public void addTask(Task task) {
        calledMethods.add("add:" + task.toString());
    }

    @Override
    public void delete(int index) {
        calledMethods.add("delete:" + index);
    }

    @Override
    public void list() {
        calledMethods.add("list");
    }

    @Override
    public void find(String input) {
        calledMethods.add("find" + input);
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>();
    }
}

package sharva.tasklist;

import sharva.exceptions.InvalidIndexException;
import sharva.tasklist.TaskListService;
import sharva.tasks.Task;
import sharva.exceptions.SharvaException;

import java.util.ArrayList;
import java.util.List;

public class TaskListStub implements TaskListService {
    private final List<String> calledMethods = new ArrayList<>();

    public List<String> getCalledMethods() {
        return calledMethods;
    }

    @Override
    public void mark(int index) throws SharvaException{
        calledMethods.add("mark:" + (index + 1));
    }

    @Override
    public void unmark(int index) throws SharvaException {
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

package sharva.message;

import sharva.tasks.Task;

public interface MessageService {
    public void sayHello();
    public void sayBye();
    public void mark(Task task);
    public void unmark(Task task);
    public void addTask(Task task, int numberOfTasks);
    public void deleteTask(Task task, int numberOfTasks);
    public void echo(String input);
}

package sharva.message;

import sharva.tasks.Task;

public interface MessageService {
    public void sayHello();
    public void markUI(Task task);
    public void unmarkUI(Task task);
    public void addTaskUI(Task task, int numberOfTasks);
    public void deleteTaskUI(Task task, int numberOfTasks);
    public void listTasksUI(String input);
    public void echoUI(String input);
}

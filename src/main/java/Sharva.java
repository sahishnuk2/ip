import java.util.Scanner;

public class Sharva {
    private static final String horizontalLine = "    _____________________________________________";
    private static final Task[] tasks = new Task[100];
    private static int taskCounter = 0;

    public static void sayHello() {
        System.out.println(horizontalLine);
        System.out.println("    Hello! I'm Sharva\n    What can I do for you?");
        System.out.println(horizontalLine);
    }

    // Parser functions
    public static void toDo(String input) throws SharvaException {
        if (input.trim().equals("todo")) {
            // for multiple spaces after todo
            throw new MissingArgumentsException("The description of a todo cannot be empty");
        }
        String taskName = input.substring(5);
        addTodo(taskName);
    }

    public static void deadline(String input) throws SharvaException {
        if (input.trim().equals("deadline")) {
            // for multiple spaces after deadline
            throw new MissingArgumentsException("The description of a deadline cannot be empty");
        }
        int byIndex = input.indexOf(" /by ");
        if (byIndex == -1) {
            throw new MissingArgumentsException("Due Date is missing!");
        }
        String taskName = input.substring(8, byIndex).trim();
        if (taskName.isEmpty()) {
            throw new MissingArgumentsException("Task name is missing!");
        }
        String by = input.substring(byIndex + 5).trim();
        if (by.isEmpty()) {
            throw new MissingArgumentsException("Due Date is missing!");
        }
        addDeadline(taskName, by);
    }

    public static void event(String input) throws SharvaException {
        if (input.trim().equals("event")) {
            // for multiple spaces after event
            throw new MissingArgumentsException("The description of a event cannot be empty");
        }
        int fromIndex = input.indexOf(" /from ");
        if (fromIndex == -1) {
            throw new MissingArgumentsException("From date is missing!");
        }
        int toIndex = input.indexOf(" /to ");
        if (toIndex == -1) {
            throw new MissingArgumentsException("To date is missing!");
        }
        if (toIndex < fromIndex) {
            throw new MissingArgumentsException("To is before from, that doesn't make sense...");
        }
        String taskName = input.substring(5, fromIndex).trim();
        if (taskName.isEmpty()) {
            throw new MissingArgumentsException("Task name is missing!");
        }
        String from = input.substring(fromIndex + 7, toIndex).trim();
        if (from.isEmpty()) {
            throw new MissingArgumentsException("From date is missing!");
        }
        String to = input.substring(toIndex + 5).trim();
        if (to.isEmpty()) {
            throw new MissingArgumentsException("To date is missing!");
        }
        addEvent(taskName, from, to);
    }

    public static void mark(String input) throws SharvaException {
        if (input.trim().equals("mark")) {
            throw new MissingArgumentsException("Which task must I mark?");
        }

        String[] strs = input.split(" ");
        if (strs.length < 2) {
            throw new InvalidCommandException();
            // For errors like marks, mark1, markee...
        } else if (strs.length > 2) {
            throw new InvalidIndexException();
        }

        if (!strs[1].matches("\\d+")) {
            throw new InvalidIndexException();
        }
        int taskNumber = Integer.parseInt(strs[1]);
        if (taskNumber <= 0 || taskNumber > taskCounter) {
            throw new InvalidIndexException();
        }
        markTask(taskNumber - 1);
    }

    public static void unmark(String input) throws SharvaException {
        if (input.trim().equals("unmark")) {
            throw new MissingArgumentsException("Which task must I unmark?");
        }

        String[] strs = input.split(" ");
        if (strs.length < 2) {
            throw new InvalidCommandException();
            // For errors like marks, mark1, markee...
        } else if (strs.length > 2) {
            throw new InvalidIndexException();
        }

        int taskNumber = Integer.parseInt(strs[1]);
        if (taskNumber <= 0 || taskNumber > taskCounter) {
            throw new InvalidIndexException();
        }
        unmarkTask(taskNumber - 1);
    }

    public static void list() {
        System.out.println(horizontalLine);
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < taskCounter; i++) {
            System.out.println("    " + String.format("%d. %s", i + 1, tasks[i]));
        }
        System.out.println(horizontalLine);
    }

    public static void handleInvalidInput(String input) throws SharvaException {
        if (input.equals("todo") || input.equals("deadline") || input.equals("event")) {
            throw new MissingArgumentsException("The description of a " + input + " cannot be empty");
        }
        throw new InvalidCommandException();
    }

    public static void sayBye() {
        System.out.println(horizontalLine);
        System.out.println("    Bye bro! See you later!");
        System.out.println(horizontalLine);
    }

    //Helper methods for marking
    private static void markTask(int index) {
        tasks[index].markAsDone();
        System.out.println(horizontalLine);
        System.out.println("    Nice! I've marked this task as done:");
        System.out.println("    " + tasks[index]);
        System.out.println(horizontalLine);
    }

    private static void unmarkTask(int index) {
        tasks[index].undoTask();
        System.out.println(horizontalLine);
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("    " + tasks[index]);
        System.out.println(horizontalLine);
    }

    // Helper methods to add tasks
    private static void addTodo(String taskName) {
        System.out.println(horizontalLine);
        Task task = new ToDo(taskName);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", taskCounter));
        System.out.println(horizontalLine);
    }

    private static void addDeadline(String taskName, String by) {
        System.out.println(horizontalLine);
        Task task = new Deadline(taskName, by);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", taskCounter));
        System.out.println(horizontalLine);
    }

    private static void addEvent(String taskName, String from, String to) {
        System.out.println(horizontalLine);
        Task task = new Event(taskName, from, to);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", taskCounter));
        System.out.println(horizontalLine);
    }

    public static void main(String[] args) {
        sayHello();
        Scanner scanner = new Scanner(System.in);
        String curr = scanner.nextLine();
        while (!curr.equals("bye")) {
            if (curr.equals("list")) {
                list();
            } else if (curr.startsWith("mark")) {
                try {
                    mark(curr);
                } catch (SharvaException e /* To change later */) {
                    System.out.println(horizontalLine);
                    System.out.println("    " + e.getMessage());
                    System.out.println(horizontalLine);
                }
            } else if (curr.startsWith("unmark")) {
                try {
                    unmark(curr);
                } catch (SharvaException e /* To change later */) {
                    System.out.println(horizontalLine);
                    System.out.println("    " + e.getMessage());
                    System.out.println(horizontalLine);
                }
            } else if (curr.startsWith("todo ")) {
                try {
                   toDo(curr);
                } catch (SharvaException e) {
                    System.out.println(horizontalLine);
                    System.out.println("    " + e.getMessage());
                    System.out.println(horizontalLine);
                }
            } else if (curr.startsWith("deadline ")) {
                try {
                    deadline(curr);
                } catch (SharvaException e) {
                    System.out.println(horizontalLine);
                    System.out.println("    " + e.getMessage());
                    System.out.println(horizontalLine);
                }
            } else if (curr.startsWith("event ")){
                try {
                    event(curr);
                } catch (SharvaException e) {
                    System.out.println(horizontalLine);
                    System.out.println("    " + e.getMessage());
                    System.out.println(horizontalLine);
                }
            } else {
                try {
                    handleInvalidInput(curr);
                } catch (SharvaException e) {
                    System.out.println(horizontalLine);
                    System.out.println("    " + e.getMessage());
                    System.out.println(horizontalLine);
                }
            }
            curr = scanner.nextLine();
        }
        sayBye();
        scanner.close();
    }
}

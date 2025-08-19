import java.util.Scanner;

public class Sharva {
    private static final String horizontalLine = "    __________________________________________";
    private static final Task[] tasks = new Task[100];
    private static int taskCounter = 0;

    public static void sayHello() {
        System.out.println(horizontalLine);
        System.out.println("    Hello! I'm Sharva\n    What can I do for you?");
        System.out.println(horizontalLine);
    }

    // Missing Index problem also need an exception

    public static void toDo(String input) throws MissingArgumentsException {
        // if all goes well, addTodo(taskName)
        if (input.trim().equals("todo")) {
            throw new MissingArgumentsException("Task name is missing!");
        }
        String taskName = input.substring(4).trim();
        addTodo(taskName);
    }

    public static void deadline(String input) throws MissingArgumentsException {
        int byIndex = input.indexOf("/by");
        if (byIndex == -1) {
            throw new MissingArgumentsException("Due Date is missing!");
        }
        String taskName = input.substring(8, byIndex).trim();
        if (taskName.isEmpty()) {
            throw new MissingArgumentsException("Task name is missing!");
        }
        String by = input.substring(byIndex + 3).trim();
        if (by.isEmpty()) {
            throw new MissingArgumentsException("By date is missing!");
        }
        addDeadline(taskName, by);
    }

    public static void event(String input) throws MissingArgumentsException {
        int fromIndex = input.indexOf("/from");
        if (fromIndex == -1) {
            throw new MissingArgumentsException("From date is missing!");
        }
        int toIndex = input.indexOf("/to");
        if (toIndex == -1) {
            throw new MissingArgumentsException("To data is missing!");
        }
        if (toIndex < fromIndex) {
            // new exception
        }
        String taskName = input.substring(5, fromIndex).trim();
        if (taskName.isEmpty()) {
            throw new MissingArgumentsException("Task name is missing!");
        }
        String from = input.substring(fromIndex + 5, toIndex).trim();
        if (from.isEmpty()) {
            throw new MissingArgumentsException("From date is missing!");
        }
        String to = input.substring(toIndex + 3).trim();
        if (to.isEmpty()) {
            throw new MissingArgumentsException("To date is missing!");
        }
        addEvent(taskName, from, to);
    }

    public static void addTodo(String taskName) {
        System.out.println(horizontalLine);
        Task task = new ToDo(taskName);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", taskCounter));
        System.out.println(horizontalLine);
    }

    public static void addDeadline(String taskName, String by) {
        System.out.println(horizontalLine);
        Task task = new Deadline(taskName, by);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", taskCounter));
        System.out.println(horizontalLine);
    }

    public static void addEvent(String taskName, String from, String to) {
        System.out.println(horizontalLine);
        Task task = new Event(taskName, from, to);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", taskCounter));
        System.out.println(horizontalLine);
    }

    public static void list() {
        System.out.println(horizontalLine);
        System.out.println("    Here are the tasks in your list:");
        for (int i = 0; i < taskCounter; i++) {
            System.out.println("    " + String.format("%d. %s", i + 1, tasks[i]));
        }
        System.out.println(horizontalLine);
    }

    public static void sayBye() {
        System.out.println(horizontalLine);
        System.out.println("    Bye bro! See you later!");
        System.out.println(horizontalLine);
    }

    public static void markTask(int index) {
        tasks[index].markAsDone();
        System.out.println(horizontalLine);
        System.out.println("    Nice! I've marked this task as done:");
        System.out.println("    " + tasks[index]);
        System.out.println(horizontalLine);
    }

    public static void unmarkTask(int index) {
        tasks[index].undoTask();
        System.out.println(horizontalLine);
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("    " + tasks[index]);
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
                int taskNumber = Integer.parseInt(curr.split(" ")[1]);
                markTask(taskNumber - 1);
            } else if (curr.startsWith("unmark")) {
                int taskNumber = Integer.parseInt(curr.split(" ")[1]);
                unmarkTask(taskNumber - 1);
            } else if (curr.startsWith("todo")) {
                try {
                   toDo(curr);
                } catch (MissingArgumentsException e) {
                    System.out.println(horizontalLine);
                    System.out.println("    " + e.getMessage());
                    System.out.println(horizontalLine);
                }
            } else if (curr.startsWith("deadline")) {
                try {
                    deadline(curr);
                } catch (MissingArgumentsException e) {
                    System.out.println(horizontalLine);
                    System.out.println("    " + e.getMessage());
                    System.out.println(horizontalLine);
                }
            } else if (curr.startsWith("event")){
                try {
                    event(curr);
                } catch (MissingArgumentsException e) {
                    System.out.println(horizontalLine);
                    System.out.println("    " + e.getMessage());
                    System.out.println(horizontalLine);
                }
            } else {
                // invalid argument
                System.out.println("Invalid Command, please try again");
            }
            curr = scanner.nextLine();
        }
        sayBye();
        scanner.close();
    }
}

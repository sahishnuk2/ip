package sharva.message;

import sharva.tasks.Task;

/**
 * Handles user interface
 */
public class Message implements MessageService {
    public static final String HORIZONTAL_LINE =
            "    __________________________________________________________________";

    /**
     * Greets the user
     */
    @Override
    public void sayHello() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    Hello! I'm Sharva\n    What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Bids farewell to the user
     */
    @Override
    public void sayBye() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    Bye bro! See you later!");
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Informs the user that the specified task is marked
     * @param task the task to be marked
     */
    @Override
    public void mark(Task task) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    Nice! I've marked this task as done:");
        System.out.println("    " + task);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Informs the user that the specified task is unmarked
     * @param task the task to be unmarked
     */
    @Override
    public void unmark(Task task) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("    " + task);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Informs the user that the specified task is added and
     * informs the user on how many tasks are in the list
     * @param task the task to be added
     * @param numberOfTasks number of tasks in the tasklist
     */
    @Override
    public void addTask(Task task, int numberOfTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.printf("    Now You have %d task(s) in the list%n", numberOfTasks);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Informs the user that the specified task is deleted and
     * informs the user on how many tasks are in the list
     * @param task the task to be deleted
     * @param numberOfTasks number of tasks in the tasklist
     */
    @Override
    public void deleteTask(Task task, int numberOfTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    Noted. I've removed this task:");
        System.out.println("    " + task);
        System.out.printf("    Now You have %d task(s) in the list%n", numberOfTasks);
        System.out.println(HORIZONTAL_LINE);
    }

    /**
     * Prints the given input string surrounded by a horizontal border.
     * @param input the string to be displayed within the border
     */
    @Override
    public void echo(String input) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(input);
        System.out.println(HORIZONTAL_LINE);
    }
}

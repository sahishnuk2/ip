public class Message {
    public static final String HORIZONTAL_LINE = "    __________________________________________________________________";

    public void sayHello() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    Hello! I'm Sharva\n    What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
    }

    public void sayBye() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    Bye bro! See you later!");
        System.out.println(HORIZONTAL_LINE);
    }

    public void mark(Task task) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    OK, I've marked this task as done:");
        System.out.println("    " + task);
        System.out.println(HORIZONTAL_LINE);
    }

    public void unmark(Task task) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    OK, I've marked this task as not done yet:");
        System.out.println("    " + task);
        System.out.println(HORIZONTAL_LINE);
    }

    public void addTask(Task task, int numberOfTasks) {
        System.out.println(HORIZONTAL_LINE);
        System.out.println("    Got it. I've added this task:");
        System.out.println("    " + task);
        System.out.println(String.format("    Now You have %d task(s) in the list", numberOfTasks));
        System.out.println(HORIZONTAL_LINE);
    }
}

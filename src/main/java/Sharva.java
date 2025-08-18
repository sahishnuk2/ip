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

    public static void addTodo(String taskName) {
        System.out.println(horizontalLine);
        Task task = new ToDo(taskName);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    added: " + task);
        System.out.println(horizontalLine);
    }

    public static void addDeadline(String taskName, String by) {
        System.out.println(horizontalLine);
        Task task = new Deadline(taskName, by);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    added: " + task);
        System.out.println(horizontalLine);
    }

    public static void addEvent(String taskName, String from, String to) {
        System.out.println(horizontalLine);
        Task task = new Event(taskName, from, to);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    added: " + task);
        System.out.println(horizontalLine);
    }

    public static void list() {
        System.out.println(horizontalLine);
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
                String[] strs = curr.split(" ");
                int len = strs.length;
                StringBuilder taskName = new StringBuilder();
                for (int i = 1; i < len; i++) {
                    taskName.append(strs[i]);
                    if (i != len - 1) {
                        taskName.append(" ");
                    }
                }
                addTodo(taskName.toString());
            } else if (curr.startsWith("deadline")) {
                // add deadline task
                String[] strs = curr.split(" ");
                int len = strs.length;
                StringBuilder taskName = new StringBuilder();
                StringBuilder by = new StringBuilder();
                int i = 1;
                while (!strs[i].equals("/by")) {
                    taskName.append(strs[i]);
                    taskName.append(" ");
                    i++;
                }
                taskName.deleteCharAt(taskName.length() - 1);
                // Now, i is at "by"
                i++;
                while (i < len) {
                    by.append(strs[i]);
                    if (i != len - 1) {
                        by.append(" ");
                    }
                    i++;
                }
                addDeadline(taskName.toString(), by.toString());
            } else if (curr.startsWith("event")){
                // add event task
                String[] strs = curr.split(" ");
                int len = strs.length;
                StringBuilder taskName = new StringBuilder();
                StringBuilder from = new StringBuilder();
                StringBuilder to = new StringBuilder();
                int i = 1;
                while (!strs[i].equals("/from")) {
                    taskName.append(strs[i]);
                    taskName.append(" ");
                    i++;
                }
                taskName.deleteCharAt(taskName.length() - 1);
                // Now, i is at "from"
                i++;

                while (!strs[i].equals("/to")) {
                    from.append(strs[i]);
                    from.append(" ");
                    i++;
                }

                from.deleteCharAt(from.length() - 1);
                // Now, i is at "to"
                i++;

                while (i < len) {
                    to.append(strs[i]);
                    if (i != len - 1) {
                        to.append(" ");
                    }
                    i++;
                }
                addEvent(taskName.toString(), from.toString(), to.toString());
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

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

    public static void addTask(String taskName) {
        System.out.println(horizontalLine);
        Task task = new Task(taskName);
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

    public static void main(String[] args) {
        sayHello();
        Scanner scanner = new Scanner(System.in);
        String curr = scanner.nextLine();
        while (!curr.equals("bye")) {
            if (curr.equals("list")) {
                list();
            } else {
                addTask(curr);
            }
            curr = scanner.nextLine();
        }
        sayBye();
        scanner.close();
    }
}

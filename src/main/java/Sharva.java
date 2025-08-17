import java.util.Scanner;

public class Sharva {
    private static final String horizontalLine = "    __________________________________________";
    private static String[] tasks = new String[100];
    private static int taskCounter = 0;

    public static void sayHello() {
        System.out.println(horizontalLine);
        System.out.println("    Hello! I'm Sharva\n    What can I do for you?");
        System.out.println(horizontalLine);
    }

    public static void addTask(String task) {
        System.out.println(horizontalLine);
        tasks[taskCounter] = task;
        taskCounter++;
        System.out.println("    added: " + task);
        System.out.println(horizontalLine);
    }

    public static void main(String[] args) {
        sayHello();
        Scanner scanner = new Scanner(System.in);
        String curr = scanner.nextLine();
        while (!curr.equals("bye")) {
            addTask(curr);
            curr = scanner.nextLine();
        }
        System.out.println("Bye bro! See you later!");
        scanner.close();
    }
}

import java.util.Scanner;

public class Sharva {
    private static final String horizontalLine = "    __________________________________________";

    public static void sayHello() {
        System.out.println(horizontalLine);
        System.out.println("    Hello! I'm Sharva\n    What can I do for you?");
        System.out.println(horizontalLine);
    }

    public static void echo(String userInput) {
        System.out.println(horizontalLine);
        System.out.println(userInput.equals("bye") ? "    Bye. Hope to see you again soon!" : "    " + userInput);
        System.out.println(horizontalLine);
    }

    public static void main(String[] args) {
        sayHello();
        Scanner scanner = new Scanner(System.in);
        String curr = scanner.nextLine();
        while (!curr.equals("bye")) {
            echo(curr);
            curr = scanner.nextLine();
        }
        echo("bye");
        scanner.close();
    }
}

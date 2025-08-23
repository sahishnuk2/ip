import java.util.Scanner;

public class Sharva {
    private final Storage storage;
    private final TaskList tasks;
    private final Message message;
    private final Parser parser;

    public Sharva(String filePath) {
        this.message = new Message();
        this.storage = new Storage(filePath, message);
        this.tasks = new TaskList(storage.load(), message);
        this.parser = new Parser(tasks);
    }

    public void run() {
        message.sayHello();
        Scanner scanner = new Scanner(System.in);
        String curr = scanner.nextLine();
        while (!curr.equals("bye")) {
            try {
                parser.parseInput(curr);
                storage.save(tasks.getTasks());
            } catch (SharvaException e) {
                message.echo("    " + e.getMessage());
            }
            curr = scanner.nextLine();
        }
        message.sayBye();
        scanner.close();
    }

    public static void main(String[] args) {
        Sharva sharva = new Sharva("./data/sharva.txt");
        sharva.run();
    }
}

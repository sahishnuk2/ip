package sharva;

import javafx.scene.layout.VBox;
import sharva.exceptions.SharvaException;
import sharva.message.Message;
import sharva.parser.Parser;
import sharva.storage.Storage;
import sharva.tasklist.TaskList;

/**
 * sharva.message.Main class used to run Sharva.
 */
public class Sharva {
    private final Storage storage;
    private TaskList tasks;
    private final Message message;
    private final Parser parser;

    /**
     * Sets up the messaging system, initialises the storage system
     * with the given filepath,loads existing tasks from the file, and handles
     * corrupted or invalid task entries if found. If loading fails, an empty
     * task list is created instead. Finally, the parser is initialised with
     * the task list.
     *
     * @param filePath the path of the file where tasks are stored
     */
    public Sharva(String filePath) {
        this.message = new Message();
        assert filePath == null && !filePath.isEmpty() : "File path cannot be null or empty";
        this.storage = new Storage(filePath);
        try {
            Storage.LoadResult result = storage.load();
            this.tasks = new TaskList(result.tasks, message);
            if (result.hasCorruptedLines()) {
                message.echo("Some tasks were removed due to file corruption: " + result.error);
            }
        } catch (SharvaException e) {
            message.echo(e.getMessage());
            this.tasks = new TaskList(message);
        }
        this.parser = new Parser(tasks);
    }

    /**
     * Runs the chatbot until the user types "bye".
     */
    public void start(VBox dialogContainer) {
        message.setDialogContainer(dialogContainer);
        message.sayHello(); // add the dialogBox to the dialogContainer

    }

    public void run(String input) {
        try {
            parser.parseInput(input);
            storage.save(tasks.getTasks());
        } catch (SharvaException e) {
            message.echo(e.getMessage());
        }
    }
}

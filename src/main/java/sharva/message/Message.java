package sharva.message;

import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import sharva.tasks.Task;

/**
 * Handles user interface
 */
public class Message implements MessageService {
    private VBox dialogContainer;

    private Image sharvaImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    public void setDialogContainer(VBox dialogContainer) {
        this.dialogContainer = dialogContainer;
    }
    /**
     * Greets the user
     */
    @Override
    public void sayHello() {
        DialogBox d = DialogBox.getSharvaDialog("Hello! I'm Sharva\nWhat can I do for you?", sharvaImage);
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Bids farewell to the user
     */
    @Override
    public void sayBye() {
        DialogBox d = DialogBox.getSharvaDialog("Bye bro! See you later!", sharvaImage);
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Informs the user that the specified task is marked
     * @param task the task to be marked
     */
    @Override
    public void mark(Task task) {
        DialogBox d = DialogBox.getSharvaDialog("Nice! I've marked this task as done:\n" + task, sharvaImage);
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Informs the user that the specified task is unmarked
     * @param task the task to be unmarked
     */
    @Override
    public void unmark(Task task) {
        DialogBox d = DialogBox.getSharvaDialog("OK, I've marked this task as not done yet:\n" + task, sharvaImage);
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Informs the user that the specified task is added and
     * informs the user on how many tasks are in the list
     * @param task the task to be added
     * @param numberOfTasks number of tasks in the tasklist
     */
    @Override
    public void addTask(Task task, int numberOfTasks) {
        String sharvaDialog = String.format("Got it. I've added this task:\n%s\nNow you have %d task(s) in the list",
                        task, numberOfTasks);
        DialogBox d = DialogBox.getSharvaDialog(sharvaDialog, sharvaImage);
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Informs the user that the specified task is deleted and
     * informs the user on how many tasks are in the list
     * @param task the task to be deleted
     * @param numberOfTasks number of tasks in the tasklist
     */
    @Override
    public void deleteTask(Task task, int numberOfTasks) {
        String sharvaDialog = String.format("Noted. I've removed this task:\n%s\nNow you have %d task(s) in the list",
                task, numberOfTasks);
        DialogBox d = DialogBox.getSharvaDialog(sharvaDialog, sharvaImage);
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Prints the given input string surrounded by a horizontal border.
     * @param input the string to be displayed within the border
     */
    @Override
    public void echo(String input) {
        DialogBox d = DialogBox.getSharvaDialog(input, sharvaImage);
        dialogContainer.getChildren().addAll(d);
    }
}

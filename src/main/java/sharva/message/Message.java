package sharva.message;

import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import sharva.tasks.Task;

/**
 * Handles user interface
 */
public class Message implements MessageService {
    private VBox dialogContainer;

    private Image sharvaImage = new Image(this.getClass().getResourceAsStream("/images/duke.png"));

    public void setDialogContainer(VBox dialogContainer) {
        assert dialogContainer != null : "Dialog container cannot be null";
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
     * Informs the user that the specified task is marked
     * @param task the task to be marked
     */
    @Override
    public void markUI(Task task) {
        DialogBox d = DialogBox.getSharvaDialog("Nice! I've marked this task as done:\n" + task, sharvaImage);
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Informs the user that the specified task is unmarked
     * @param task the task to be unmarked
     */
    @Override
    public void unmarkUI(Task task) {
        DialogBox d = DialogBox.getSharvaDialog("OK, I've marked this task as not done yet:\n" + task, sharvaImage);
        d.getStyleClass().add("orange");
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Informs the user that the specified task is added and
     * informs the user on how many tasks are in the list
     * @param task the task to be added
     * @param numberOfTasks number of tasks in the tasklist
     */
    @Override
    public void addTaskUI(Task task, int numberOfTasks) {
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
    public void deleteTaskUI(Task task, int numberOfTasks) {
        String sharvaDialog = String.format("Noted. I've removed this task:\n%s\nNow you have %d task(s) in the list",
                task, numberOfTasks);
        DialogBox d = DialogBox.getSharvaDialog(sharvaDialog, sharvaImage);
        d.getStyleClass().add("orange");
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Prints the given input string in a dialog box
     * @param input list of tasks to be displayed within the dialog box
     */
    @Override
    public void listTasksUI(String input) {
        DialogBox d = DialogBox.getSharvaDialog(input, sharvaImage);
        d.getStyleClass().add("yellow");
        dialogContainer.getChildren().addAll(d);
    }

    /**
     * Prints the given input string in a dialog box
     * @param input the string to be displayed within the dialog box
     */
    @Override
    public void echoUI(String input) {
        DialogBox d = DialogBox.getSharvaDialog(input, sharvaImage);
        d.getStyleClass().add("red");
        dialogContainer.getChildren().addAll(d);
    }
}

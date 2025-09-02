package sharva.message;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sharva.Sharva;

public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Sharva sharva;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/DaDuke.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    public void setSharva(Sharva sharva) {
        this.sharva = sharva;
        this.sharva.start(this.dialogContainer);
    }

    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        if (!userText.isEmpty()) {
            dialogContainer.getChildren().addAll(
                    DialogBox.getUserDialog(userText, userImage)
            );
            sharva.run(userText);
            userInput.clear();
        }
    }
}

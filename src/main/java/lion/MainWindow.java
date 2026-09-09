package lion;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Lion lion;

    private Image userImage =
            new Image(this.getClass().getResourceAsStream("/images/DaUser.png"));

    private Image lionImage =
            new Image(this.getClass().getResourceAsStream("/images/DaLion.png"));

    /** Keeps the scroll pane pinned to the latest dialog as new ones are added. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Lion instance this window sends commands to.
     *
     * @param lion Lion instance backing this window.
     */
    public void setLion(Lion lion) {
        this.lion = lion;
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = lion.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getLionDialog(response, lionImage)
        );

        userInput.clear();
    }
}

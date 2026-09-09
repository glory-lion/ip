package lion;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView and a text label.
 */
public class DialogBox extends HBox {

    @FXML
    private Label dialog;

    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    private void flip() {
        ObservableList<Node> reversedChildren =
                FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(reversedChildren);
        getChildren().setAll(reversedChildren);
        setAlignment(Pos.TOP_LEFT);

        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a dialog box showing the user's message, aligned to the right.
     *
     * @param text message text to display.
     * @param img user's display picture.
     * @return dialog box in the user's default (unflipped) layout.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        return new DialogBox(text, img);
    }

    /**
     * Creates a dialog box showing Lion's reply, flipped to the left.
     *
     * @param text message text to display.
     * @param img Lion's display picture.
     * @return dialog box flipped so its picture and text swap sides.
     */
    public static DialogBox getLionDialog(String text, Image img) {
        DialogBox dialogBox = new DialogBox(text, img);
        dialogBox.flip();
        return dialogBox;
    }
}

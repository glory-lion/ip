package lion;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Lion using FXML.
 */
public class Main extends Application {

    private static final double MIN_WINDOW_HEIGHT = 220;
    private static final double MIN_WINDOW_WIDTH = 417;

    private Lion lion = new Lion();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader =
                    new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));

            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            stage.setScene(scene);

            fxmlLoader.<MainWindow>getController().setLion(lion);

            stage.setMinHeight(MIN_WINDOW_HEIGHT);
            stage.setMinWidth(MIN_WINDOW_WIDTH);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

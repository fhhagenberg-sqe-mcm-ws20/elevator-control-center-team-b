package at.fhhagenberg.sqe;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        var layout = new BorderPane();
        var button = new Button("Click me!");
        button.setOnAction(evt -> button.setText("Clicked!"));
        layout.setBottom(button);

        var scene = new Scene(layout, 640, 480);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
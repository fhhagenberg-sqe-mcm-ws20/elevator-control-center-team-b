package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.converter.ModelConverter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Generated;
import sqelevator.IElevator;
import sqelevator.MockElevator;

/**
 * JavaFX App
 */
public class App extends Application {

    private final IElevator system = new MockElevator();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Parent root = mainLoader.load();
        primaryStage.setTitle("Elevator System");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        ModelConverter converter = new ModelConverter(system);

        ((MainController) mainLoader.getController()).initModel(converter.init());
    }

    @Generated
    public static void main(String[] args) {
        launch(args);
    }
}
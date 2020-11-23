package at.fhhagenberg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Generated;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Parent root = mainLoader.load();
        primaryStage.setTitle("Elevator System");
        Scene scene = new Scene(root, 800, 600);
        //TODO: Decide which layout we want to take
        //JMetro jMetro = new JMetro(Style.LIGHT);
        //jMetro.setScene(scene);
        //root.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //((MainController) mainLoader.getController()).initModel(system);
    }

    @Generated
    public static void main(String[] args) {
        launch(args);
    }
}
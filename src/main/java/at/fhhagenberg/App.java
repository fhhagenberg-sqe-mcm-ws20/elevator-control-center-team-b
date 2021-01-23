package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.converter.ModelConverter;
import at.fhhagenberg.model.Building;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Generated;
import lombok.SneakyThrows;
import sqelevator.IElevator;

import java.io.IOException;
import java.rmi.Naming;

/**
 * JavaFX App
 */
public class App extends Application {
    //private final IElevator system = new MockElevator();
    private final ElevatorControlSystem elevatorControlSystem;
    private Building building;
    private Boolean error = false;

    public App(){
        elevatorControlSystem = new ElevatorControlSystem("rmi://localhost/ElevatorSim");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Parent root = mainLoader.load();
        primaryStage.setTitle("Elevator System");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        building = new Building();
        //TODO check if controlsystem is null
        ((MainController) mainLoader.getController()).initModel(elevatorControlSystem.initBuilding());

        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (!error) {
                    if (!elevatorControlSystem.getSystemConnected()) {
                        Platform.runLater(() -> ((MainController) mainLoader.getController()).status_label.setText("Connecting..."));
                    } else {
                        Platform.runLater(() -> {
                            ((MainController) mainLoader.getController()).status_label.setText("System connected!");
                            elevatorControlSystem.update(building);
                        /*try {
                            ((MainController) mainLoader.getController()).updateModel();
                        } catch (IOException e) {
                            e.printStackTrace();
                            error = true;
                        }*/
                        });
                    }
                    Thread.sleep(25);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

    @Generated
    public static void main(String[] args) {
        launch(args);
    }
}
package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.converter.ModelConverter;
import at.fhhagenberg.model.Building;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private boolean error = false;
    private boolean firstConnectionEstablished = false;
    Thread thread;

    public App() {
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
        Runnable runnable = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (!error) {
                    if (!elevatorControlSystem.getSystemConnected().get()) {
                        Platform.runLater(() -> ((MainController) mainLoader.getController()).status_label.setText("Connecting..."));
                    } else {
                        if (firstConnectionEstablished) {
                            Platform.runLater(() -> {
                                ((MainController) mainLoader.getController()).status_label.setText("System connected!");
                                elevatorControlSystem.update(building);
                            });
                        }
                    }
                    Thread.sleep(100);
                }
            }
        };
        thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();

        elevatorControlSystem.getSystemConnected().addListener(new ChangeListener<Boolean>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (newValue && !firstConnectionEstablished) {
                    building = elevatorControlSystem.initBuilding();
                    Platform.runLater(() -> {
                        try {
                            ((MainController) mainLoader.getController()).initModel(building);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    firstConnectionEstablished = true;
                } else if (!newValue) {
                    thread.wait();
                    Platform.runLater(() -> ((MainController) mainLoader.getController()).status_label.setText("Connecting..."));
                } else {
                    thread.notify();
                }
            }
        });
    }

    @Generated
    public static void main(String[] args) {
        launch(args);
    }
}
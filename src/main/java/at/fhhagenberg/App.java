package at.fhhagenberg;

import at.fhhagenberg.controller.GuiConstants;
import at.fhhagenberg.controller.MainController;
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

/**
 * JavaFX App
 */
public class App extends Application {
    //private final IElevator system = new MockElevator();
    private final ElevatorControlSystem elevatorControlSystem;
    private Building building;
    private final boolean error = false;
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
                    if (elevatorControlSystem.getSystemConnected().get()) {
                        Platform.runLater(() -> {
                            elevatorControlSystem.update(building);
                        });
                    }
                    Thread.sleep(250);
                }
            }
        };
        thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();

        MainController mainController = mainLoader.getController();
        elevatorControlSystem.getSystemConnected().addListener(new ChangeListener<Boolean>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    building = elevatorControlSystem.initBuilding();
                    setStatusUI(mainController, true);
                    ((MainController) mainLoader.getController()).initModel(building);
                } else {
                    setStatusUI(mainController, false);
                }
            }
        });
        setStatusUI(mainController, elevatorControlSystem.getSystemConnected().get());
    }

    public void setStatusUI(MainController mainController, boolean isConnected) {
        Platform.runLater(() -> {
            String text;
            String style;
            if (isConnected) {
                text = "System Connected.";
                style = GuiConstants.STATUS_OK_STYLE;
            } else {
                text = "System tries to connect..";
                style = GuiConstants.ERROR_STYLE;
            }
            mainController.systemCanBeChanged(isConnected);
            mainController.status_label.setText(text);
            mainController.status_label.getStyleClass().removeAll(mainController.status_label.getStyleClass());
            mainController.status_label.getStyleClass().add(style);
        });
    }

    @Generated
    public static void main(String[] args) {
        launch(args);
    }
}
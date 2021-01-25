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
import lombok.Getter;
import lombok.SneakyThrows;

/**
 * JavaFX App
 */
public class App extends Application {

    //private final IElevator system = new MockElevator();
    @Getter
    private final ElevatorControlSystem elevatorControlSystem;
    private Building building;
    private Thread thread;
    private final boolean error = false;
    private final RemoteExceptionHandler handler = RemoteExceptionHandler.instance();
    private MainController mainController;

    public App() {
        elevatorControlSystem = new ElevatorControlSystem("rmi://localhost/ElevatorSim");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Parent root = mainLoader.load();

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        primaryStage.setTitle("Elevator System");
        primaryStage.setScene(scene);
        primaryStage.show();

        handler.addObserver(elevatorControlSystem);
        mainController = mainLoader.getController();

        thread = new Thread(() -> {
            while (!error) {
                if (elevatorControlSystem.getSystemConnected().get()) {
                    Platform.runLater(() -> elevatorControlSystem.update(building, mainController));
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();

        Thread t = new Thread(() -> {
            while (!error) {
                if (elevatorControlSystem.getSystemConnected().get()) {
                    Platform.runLater(() -> elevatorControlSystem.updateMode(building, mainController));
                }
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        t.start();

        elevatorControlSystem.getSystemConnected().addListener(new ChangeListener<Boolean>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    building = elevatorControlSystem.initBuilding();
                    setStatusUI(true);
                    ((MainController) mainLoader.getController()).initModel(building);
                } else {
                    setStatusUI(false);
                }
            }
        });
        setStatusUI(elevatorControlSystem.getSystemConnected().get());
    }

    public void setStatusUI(boolean isConnected) {
        Platform.runLater(() -> {
            String text;
            String style;
            if (isConnected) {
                text = GuiConstants.MSG_IS_CONNECTED;
                style = GuiConstants.STATUS_OK_STYLE;
            } else {
                text = GuiConstants.MSG_CONNECTING;
                style = GuiConstants.ERROR_STYLE;
            }
            mainController.systemCanBeChanged(isConnected);
            mainController.statusLabel.setText(text);
            mainController.statusLabel.getStyleClass().removeAll(mainController.statusLabel.getStyleClass());
            mainController.statusLabel.getStyleClass().add(style);
        });
    }

    @Generated
    public static void main(String[] args) {
        launch(args);
    }
}
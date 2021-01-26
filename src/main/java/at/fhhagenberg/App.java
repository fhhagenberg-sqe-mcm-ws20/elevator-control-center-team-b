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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lombok.Generated;
import lombok.Getter;
import lombok.SneakyThrows;
import sqelevator.IElevator;

/**
 * JavaFX App
 */
public class App extends Application {

    @Getter
    private final ElevatorControlSystem elevatorControlSystem;
    private Building building;
    private static final boolean ERROR = false;
    private final RemoteExceptionHandler handler = RemoteExceptionHandler.instance();
    private MainController mainController;

    Alert connectionErrorDialog = new Alert(Alert.AlertType.ERROR);


    public App() {
        elevatorControlSystem = new ElevatorControlSystem("rmi://localhost/ElevatorSim");
    }

    public App(IElevator mockElevator, Building testBuilding) {
        elevatorControlSystem = new ElevatorControlSystem(mockElevator);
        building = testBuilding;
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
        createConnectionErrorDialog();

        setupThreads();

        elevatorControlSystem.getSystemConnected().addListener(new ChangeListener<Boolean>() {
            @SneakyThrows
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                if (Boolean.TRUE.equals(newValue)) {
                    setStatusUI(true);
                    building = elevatorControlSystem.initBuilding();
                    ((MainController) mainLoader.getController()).setModel(building);
                } else {
                    setStatusUI(false);
                }
            }
        });
        setStatusUI(elevatorControlSystem.getSystemConnected().get());
    }

    public void setupThreads() {
        Thread dataUpdateThread = new Thread(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                while (!ERROR) {
                    if (elevatorControlSystem.getSystemConnected().get()) {
                        Platform.runLater(() -> elevatorControlSystem.update(building, mainController));
                    }
                    Thread.sleep(1000);
                }
            }
        });

        Thread autoUpdateThread = new Thread(new Runnable() {
            @Override
            @SneakyThrows
            public void run() {
                while (!ERROR) {
                    if (elevatorControlSystem.getSystemConnected().get()) {
                        Platform.runLater(() -> elevatorControlSystem.updateMode(building, mainController));
                    }
                    Thread.sleep(1250);
                }
            }
        });

        dataUpdateThread.setDaemon(true);
        autoUpdateThread.setDaemon(true);
        dataUpdateThread.start();
        autoUpdateThread.start();
    }

    public void setStatusUI(boolean isConnected) {
        Platform.runLater(() -> {
            String text;
            String style;
            if (isConnected) {
                text = GuiConstants.MSG_IS_CONNECTED;
                style = GuiConstants.STATUS_OK_STYLE;
                if (connectionErrorDialog.isShowing()) {
                    connectionErrorDialog.hide();
                    connectionErrorDialog.close();
                }
            } else {
                text = GuiConstants.MSG_CONNECTING;
                style = GuiConstants.ERROR_STYLE;
                showErrorDialog();
            }
            mainController.systemCanBeChanged(isConnected);
            mainController.statusLabel.setText(text);
            mainController.statusLabel.getStyleClass().removeAll(mainController.statusLabel.getStyleClass());
            mainController.statusLabel.getStyleClass().add(style);
        });
    }

    public void createConnectionErrorDialog() {
        connectionErrorDialog = new Alert(Alert.AlertType.ERROR);
        connectionErrorDialog.setTitle("Connection Error");
        connectionErrorDialog.setHeaderText("The connection to the elevator system is not established.");
        connectionErrorDialog.setContentText("The system is trying to connect..");
        ButtonType buttonTypeCancel = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        connectionErrorDialog.getButtonTypes().setAll(buttonTypeCancel);
    }

    public void showErrorDialog() {
        if (!connectionErrorDialog.isShowing()) {
            connectionErrorDialog.show();
        }
    }

    @Generated
    public static void main(String[] args) {
        launch(args);
    }
}
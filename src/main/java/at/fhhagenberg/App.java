package at.fhhagenberg;

import at.fhhagenberg.controller.MainController;
import at.fhhagenberg.model.*;
import at.fhhagenberg.sqe.IElevator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Generated;

import java.util.Arrays;

/**
 * JavaFX App
 */
public class App extends Application {

    private IElevator system;
    private IBuildingElevator[] elevators;
    private IFloor[] floors;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Parent root = mainLoader.load();
        primaryStage.setTitle("Elevator System");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Mock data
        boolean[] temp = new boolean[5];
        boolean[] tempButton = new boolean[5];
        Arrays.fill(temp, true);
        tempButton[0] = true;

        floors = new IFloor[5];
        floors[0] = new Floor(false, true);
        floors[1] = new Floor(false, false);
        floors[2] = new Floor(false, false);
        floors[3] = new Floor(false, false);
        floors[4] = new Floor(false, false);

        elevators = new Elevator[9];
        elevators[0] = new Elevator(5, 200, 300);
        elevators[1] = new Elevator(IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 1500, 10, temp, 2);
        elevators[2] = new Elevator(IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 1500, 10, temp, 0);
        elevators[3] = new Elevator(5, 250, 300);
        elevators[4] = new Elevator(IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 1500, 10, temp, 4);
        elevators[5] = new Elevator(IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 1500, 10, temp, 0);
        elevators[6] = new Elevator(5, 150, 300);
        elevators[7] = new Elevator(IBuildingElevator.Direction_State.DOWN.value(), 2, tempButton, IBuildingElevator.Door_State.CLOSED.value(), 3, 30, 2, 360, 300, temp, 2);
        elevators[8] = new Elevator(IBuildingElevator.Direction_State.UNCOMMITTED.value(), 2, new boolean[5], IBuildingElevator.Door_State.OPEN.value(), 1, 10, 0, 420, 400, temp, 0);

        Building mockBuilding = new Building(3, 10, 5, elevators, floors);
        system = new ElevatorSystem(mockBuilding, 100L);

        ((MainController) mainLoader.getController()).initModel(system);
    }

    @Generated
    public static void main(String[] args) {
        launch(args);
    }
}
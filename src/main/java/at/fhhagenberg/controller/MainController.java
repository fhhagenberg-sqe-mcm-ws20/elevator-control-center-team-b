package at.fhhagenberg.controller;

import at.fhhagenberg.model.ElevatorSystem;
import at.fhhagenberg.model.IBuildingElevator;
import at.fhhagenberg.sqe.IElevator;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    public Label status_label;
    public JFXToggleButton mode_button;
    public JFXMasonryPane elevator_view;
    public VBox left_menu;

    private ElevatorSystem elevatorSystem;
    private final ArrayList<ElevatorController> elevatorControllers = new ArrayList<>();

    public void initialize() {
        mode_button.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue){
                mode_button.setText("Auto");
            }else{
                mode_button.setText("Manual");
            }
            if (!elevatorControllers.isEmpty()) {
                for (ElevatorController elevatorController : elevatorControllers) {
                    elevatorController.setAutoMode(newValue);
                }
            }
        }));
    }

    /**
     * Method to initialize the model
     *
     * @param elevatorSystem the ElevatorSystem
     */
    public void initModel(IElevator elevatorSystem) throws IOException {
        if (this.elevatorSystem != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        // Needed for JMetro layout
        //mode_button.getStyleClass().add("jfx-toggle-button");
        this.elevatorSystem = (ElevatorSystem) elevatorSystem;
        displayElevatorControllers();
    }

    /**
     * Method to initialize the elevator views
     *
     * @throws IOException
     */
    public void displayElevatorControllers() throws IOException {
        FXMLLoader elevatorLoader;
        for (int i = 0; i < this.elevatorSystem.getBuilding().getElevators().length; i++) {
            elevatorLoader = new FXMLLoader(getClass().getResource("/Elevator.fxml"));
            IBuildingElevator elevator = this.elevatorSystem.getBuilding().getElevators()[i];
            AnchorPane elevatorAnchorPane = elevatorLoader.load();
            elevatorAnchorPane.setId("elevator" + i);
            ElevatorController elevatorController = elevatorLoader.getController();
            elevatorController.initModel(elevator, i, this.elevatorSystem.getBuilding().getFloorCount(), left_menu);
            elevatorControllers.add(elevatorController);
            elevator_view.getChildren().add(elevatorAnchorPane);
        }
    }
}

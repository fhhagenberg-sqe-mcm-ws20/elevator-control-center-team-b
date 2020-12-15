package at.fhhagenberg.controller;

import at.fhhagenberg.model.Building;
import at.fhhagenberg.model.IBuildingElevator;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;

public class MainController {
    public Label status_label;
    public JFXToggleButton mode_button;
    public JFXMasonryPane elevator_view;
    public VBox left_menu;
    public VBox warning_box;
    public VBox error_box;
    public VBox floor_list_right;

    private Building building;
    private final ArrayList<ElevatorController> elevatorControllers = new ArrayList<>();

    public ArrayList<ElevatorController> getElevatorControllers() {
        return elevatorControllers;
    }

    private static final String ROUND_BUTTON_STYLE = "round-button";


    public void initialize() {
        mode_button.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                mode_button.setText("Auto");
            } else {
                mode_button.setText("Manual");
            }
            for (ElevatorController elevatorController : elevatorControllers) {
                elevatorController.setAutoMode(newValue);
            }
        }));
    }

    /**
     * Method to initialize the model
     *
     * @param building the data
     */
    public void initModel(Building building) throws IOException {
        if (this.building != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }

        this.building = building;
        displayElevatorControllers();
    }

    /**
     * Method to initialize the elevator views
     *
     * @throws IOException if no resource is found
     */
    public void displayElevatorControllers() throws IOException {
        FXMLLoader elevatorLoader;
        for (int i = 0; i < this.building.getElevators().length; i++) {
            elevatorLoader = new FXMLLoader(getClass().getResource("/Elevator.fxml"));
            IBuildingElevator elevator = this.building.getElevators()[i];
            AnchorPane elevatorAnchorPane = elevatorLoader.load();
            elevatorAnchorPane.setId("elevator" + i);
            ElevatorController elevatorController = elevatorLoader.getController();
            elevatorController.initModel(elevator, i, this.building.getFloorCount(), left_menu);
            elevatorControllers.add(elevatorController);
            elevator_view.getChildren().add(elevatorAnchorPane);
        }
        for (int i = 0; i < building.getFloorCount(); i++) {
            floor_list_right.getChildren().add(createFloorDisplay(building.getFloors()[i].getNumber()));
        }
    }

    /**
     * Method to create a floor label
     */
    public Node createFloorDisplay(int floorNumber) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        Label floorLabel = new Label(floorNumber + " Floor");
        gridPane.add(floorLabel, 0, 0, 1, 2);

        FontAwesomeIconView arrowUpIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_UP);
        arrowUpIcon.getStyleClass().add("right-side-icon");
        gridPane.add(arrowUpIcon, 1, 0, 1, 1);

        FontAwesomeIconView arrowDownIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_DOWN);
        arrowDownIcon.getStyleClass().add("right-side-icon");
        gridPane.add(arrowDownIcon, 1, 1, 1, 1);

        if (building.getFloors()[floorNumber].isUpButton()) {
            arrowUpIcon.getStyleClass().add("clicked");
        }
        if (building.getFloors()[floorNumber].isDownButton()) {
            arrowDownIcon.getStyleClass().add("clicked");
        }
        gridPane.getStyleClass().add(ROUND_BUTTON_STYLE);
        return gridPane;
    }
}

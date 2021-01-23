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

import static at.fhhagenberg.controller.GuiConstants.*;

public class MainController {
    public Label status_label;
    public JFXToggleButton mode_button;
    public JFXMasonryPane elevatorView;
    public VBox leftMenu;
    public VBox warningBox;
    public VBox errorBox;
    public VBox floorListRight;

    private Building building;
    private ArrayList<ElevatorController> elevatorControllers = new ArrayList<>();

    public ArrayList<ElevatorController> getElevatorControllers() {
        return elevatorControllers;
    }

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

    public void updateModel() throws IOException {
        this.elevatorControllers = new ArrayList<>();
        displayElevatorControllers();
    }

    /**
     * Method to initialize the elevator views
     *
     * @throws IOException if no resource is found
     */
    public void displayElevatorControllers() throws IOException {
        FXMLLoader elevatorLoader;
        for (int i = 0; i < this.building.getElevators().size(); i++) {
            elevatorLoader = new FXMLLoader(getClass().getResource("/Elevator.fxml"));
            IBuildingElevator elevator = this.building.getElevators().get(i);
            AnchorPane elevatorAnchorPane = elevatorLoader.load();
            elevatorAnchorPane.setId(ELEVATOR_ID_PREFIX + i);
            ElevatorController elevatorController = elevatorLoader.getController();
            elevatorController.initModel(elevator, i, this.building.getFloorCount(), leftMenu);
            elevatorControllers.add(elevatorController);
            elevatorView.getChildren().add(elevatorAnchorPane);
        }
        for (int i = 0; i < building.getFloorCount(); i++) {
            floorListRight.getChildren().add(createFloorDisplay(building.getFloors().get(i).getNumber()));
        }
    }

    public void updateElevatorControllers() throws IOException{
        for(int i = 0; i < this.building.getElevators().size(); i++){

        }
    }

    /**
     * Method to create a floor label
     */
    public Node createFloorDisplay(int floorNumber) {
        GridPane gridPane = new GridPane();
        gridPane.setId(FLOOR_BTN_ID_PREFIX + floorNumber);
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        Label floorLabel = new Label(floorNumber + " Floor");
        gridPane.add(floorLabel, 0, 0, 1, 2);

        FontAwesomeIconView arrowUpIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_UP);
        arrowUpIcon.setId(UP_ARROW_ID);
        arrowUpIcon.getStyleClass().add(RIGHT_SIDE_ICON_STYLE);
        if (building.getFloors().get(floorNumber).getUpButtonProperty().get()) {
            arrowUpIcon.getStyleClass().add(CLICKED_STYLE);
        }
        gridPane.add(arrowUpIcon, 1, 0, 1, 1);
        building.getFloors().get(floorNumber).getUpButtonProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                arrowUpIcon.getStyleClass().add(CLICKED_STYLE);
            } else {
                arrowUpIcon.getStyleClass().remove(CLICKED_STYLE);
            }
        });

        FontAwesomeIconView arrowDownIcon = new FontAwesomeIconView(FontAwesomeIcon.ARROW_DOWN);
        arrowDownIcon.setId(DOWN_ARROW_ID);
        arrowDownIcon.getStyleClass().add(RIGHT_SIDE_ICON_STYLE);
        if (building.getFloors().get(floorNumber).getDownButtonProperty().get()) {
            arrowDownIcon.getStyleClass().add(CLICKED_STYLE);
        }
        gridPane.add(arrowDownIcon, 1, 1, 1, 1);
        building.getFloors().get(floorNumber).getDownButtonProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue) {
                arrowDownIcon.getStyleClass().add(CLICKED_STYLE);
            } else {
                arrowDownIcon.getStyleClass().remove(CLICKED_STYLE);
            }
        });

        gridPane.getStyleClass().add(ROUND_BUTTON_STYLE);
        return gridPane;
    }
}
